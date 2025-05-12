package am.banking.system.user.infrastructure.security.client;

import am.banking.system.common.dto.UserDto;
import am.banking.system.common.dto.security.TokenResponse;
import am.banking.system.common.dto.security.TokenValidatorRequest;
import am.banking.system.user.infrastructure.security.abstraction.IUserTokenServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:29:29
 */
@Service
@RequiredArgsConstructor
public class UserTokenServiceClient implements IUserTokenServiceClient {
    private final WebClient webClient;

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public TokenResponse generateEmailVerificationToken(UserDto user) {
        return webClient.post()
                .uri("/api/security/generate-email-verification-token")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Boolean validateEmailVerificationToken(String token, String username) {
        return webClient.post()
                .uri("/api/security/validate-email-verification-token")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public TokenResponse generatePasswordRecoveryToken(UserDto user) {
        return webClient.post()
                .uri("/api/security/generate-password-recovery-token")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Boolean validatePasswordRecoveryToken(String token, String username) {
        return webClient.post()
                .uri("/api/security/validate-password-recovery-token")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public String invalidateUsedToken(String token) {
        return webClient.post()
                .uri("/api/security/invalidate-used-token")
                .bodyValue(new TokenResponse(token))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}