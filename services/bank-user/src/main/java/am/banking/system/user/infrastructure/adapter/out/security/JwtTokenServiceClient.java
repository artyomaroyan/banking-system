package am.banking.system.user.infrastructure.adapter.out.security;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.security.TokenValidatorRequest;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.common.infrastructure.tls.configuration.InternalSecretProperties;
import am.banking.system.common.shared.exception.security.EmptyTokenException;
import am.banking.system.common.shared.exception.security.TimeoutException;
import am.banking.system.common.shared.exception.security.TokenGenerationException;
import am.banking.system.user.application.port.out.JwtTokenServiceClientPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:26:27
 */
@Slf4j
@Service
public class JwtTokenServiceClient implements JwtTokenServiceClientPort {
    private final WebClient webClient;
    private final InternalSecretProperties secretProperties;

    public JwtTokenServiceClient(@Qualifier("securedWebClient") WebClient webClient, InternalSecretProperties secretProperties) {
        this.webClient = webClient;
        this.secretProperties = secretProperties;
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<TokenResponse> generateJwtToken(@Valid UserDto user) {
        return webClient.post()
                .uri("/api/internal/security/jwt/generate")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnError(error -> log.error("Unable to generate token: {}", error.getMessage(), error));
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<String> generateSystemToken() {
        log.info("Sending user request with internal secret: {}", secretProperties.secret());
        return webClient.get()
                .uri("/api/v1/secure/local/system-token")
                .headers(header -> header.set("X-Internal-Secret", secretProperties.secret()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    log.error("Failed to get system token. Status: {}", response.statusCode());

                    return response.bodyToMono(String.class)
                            .defaultIfEmpty("No error body")
                            .flatMap(body -> Mono.error(new TokenGenerationException(
                                    "System token request failed: " + response.statusCode() + " - " +  body)));
                })
                .bodyToMono(String.class)
                .doOnNext(token -> {
                    if (token == null || token.isEmpty() || token.trim().isBlank()) {
                        log.error("Received empty system token");
                        throw new EmptyTokenException("Received empty system token");
                    }
                    log.info("Received System token: {}", token);
                })
                .timeout(Duration.ofSeconds(10),
                        Mono.error(new TimeoutException("System token generation timed out")));
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Boolean> validateJwtToken(String token, String username) {
        return webClient.post()
                .uri("/api/internal/security/jwt/validate")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(error -> log.error("Unable to validate token: {}", error.getMessage(), error));
    }
}