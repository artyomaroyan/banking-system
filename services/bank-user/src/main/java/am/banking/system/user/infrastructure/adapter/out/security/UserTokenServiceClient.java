package am.banking.system.user.infrastructure.adapter.out.security;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.security.TokenValidatorRequest;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.user.application.port.out.UserTokenServiceClientPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:29:29
 */
@Slf4j
@Service
public class UserTokenServiceClient implements UserTokenServiceClientPort {
    private final WebClient webClient;

    public UserTokenServiceClient(@Qualifier("securedWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @PostConstruct
    void logWebClientType() {
        log.info("Custom Log:: Logging WebClient type: {}", webClient.getClass().getSimpleName());
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<TokenResponse> generateEmailVerificationToken(UserDto user) {
        return webClient.post()
                .uri("/api/security/web/generate-email-verification-token")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class);
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Boolean> validateEmailVerificationToken(String token, String username) {
        return webClient.post()
                .uri("/api/security/web/validate-email-verification-token")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<TokenResponse> generatePasswordRecoveryToken(UserDto user) {
        return webClient.post()
                .uri("/api/security/web/generate-password-recovery-token")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Boolean> validatePasswordRecoveryToken(String token, String username) {
        return webClient.post()
                .uri("/api/security/web/validate-password-recovery-token")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Void> invalidateUsedToken(String token) {
        return webClient.post()
                .uri("/api/security/web/invalidate-used-token")
                .bodyValue(new TokenResponse(token))
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }
}