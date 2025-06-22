package am.banking.system.user.infrastructure.adapter.out.security;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.security.TokenValidatorRequest;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.common.infrastructure.tls.configuration.InternalSecretProperties;
import am.banking.system.user.application.port.out.JwtTokenServiceClientPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    @PostConstruct
    void logWebClientType() {
        log.info("Custom Log:: Logging WebClient type: {}", webClient.getClass().getSimpleName());
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<TokenResponse> generateJwtToken(@Valid UserDto user) {
        return webClient.post()
                .uri("/api/internal/security/generate-jwt-token")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<String> generateSystemToken() {
        return webClient.get()
                .uri("/api/v1/secure/local/system-token")
                .headers(header -> header.set("X-Internal-Secret", secretProperties.secret()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    log.error("Failed to get system token. Status: {}", response.statusCode());
                    return response.bodyToMono(String.class)
                            .defaultIfEmpty("No error body")
                            .flatMap(body -> Mono.error(new RuntimeException("System token request failed" + body)));
                })
                .bodyToMono(String.class)
                .doOnNext(token -> log.info("System token received: {}", token))
                .switchIfEmpty(Mono.error(new IllegalStateException("System token generation returned empty")));
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Boolean> validateJwtToken(String token, String username) {
        return webClient.post()
                .uri("/api/internal/security/validate-jwt-token")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(error -> log.error("Custom Log:: unable to validate token: {}", error.getMessage(), error));
    }
}