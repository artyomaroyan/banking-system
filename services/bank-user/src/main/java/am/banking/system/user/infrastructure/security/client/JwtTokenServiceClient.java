package am.banking.system.user.infrastructure.security.client;

import am.banking.system.common.dto.security.TokenResponse;
import am.banking.system.common.dto.security.TokenValidatorRequest;
import am.banking.system.common.dto.user.UserDto;
import am.banking.system.common.tls.configuration.InternalSecretProperties;
import am.banking.system.user.infrastructure.security.abstraction.IJwtTokenServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class JwtTokenServiceClient implements IJwtTokenServiceClient {
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
                .uri("/api/security/web/generate-jwt-token")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<String> generateSystemToken() {
        return webClient.post()
                .uri("/api/v1/secure/local/system-token")
                .headers(header -> header.set("X-Internal-Secret", secretProperties.secret()))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    log.error("Error generating system token: {}", e.getMessage());
                    return Mono.error(new RuntimeException("Failed to generate system token"));
                });


//        return webClient.post()
//                .uri("/api/v1/secure/local/system-token")
//                .headers(headers -> headers.set("X-Internal-Secret", secretProperties.secret()))
//                .retrieve()
//                .onStatus(status -> status == FORBIDDEN,
//                        response -> response.bodyToMono(String.class)
//                                .flatMap(error -> {
//                                    log.error("Custom Log:: Forbidden error response body: {}", error);
//                                    return Mono.error(new RuntimeException("Forbidden: " + error));
//                                }))
//                .bodyToMono(String.class);
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Boolean> validateJwtToken(String token, String username) {
        return webClient.post()
                .uri("/api/security/web/validate-jwt-token")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(error -> log.error("Custom Log:: unable to validate token: {}", error.getMessage(), error));
    }
}