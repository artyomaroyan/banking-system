package am.banking.system.user.infrastructure.client;

import am.banking.system.common.dto.*;
import am.banking.system.common.enums.PermissionEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 20.04.25
 * Time: 01:37:13
 */
@Service
@RequiredArgsConstructor
public class SecurityServiceClient {
    private final WebClient webClient;

    public Mono<String> hashPassword(String password) {
        return webClient.post()
                .uri("/api/security/hash-password")
                .bodyValue(new PasswordHashingRequest(password))
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<Boolean> validatePassword(String rawPassword, String hashedPassword) {
        return webClient.post()
                .uri("/api/security/validate-password")
                .bodyValue(new PasswordValidatorRequest(rawPassword, hashedPassword))
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public Mono<TokenResponse> generateJwtToken(@Valid UserDto user) {
        return webClient.post()
                .uri("/api/security/generate-jwt-token")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class);
    }

    public Mono<Boolean> validateJwtToken(String token, String username) {
        return webClient.post()
                .uri("/api/security/validate-jwt-token")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public Mono<TokenResponse> generateEmailVerificationToken(@Valid UserDto user) {
        return webClient.post()
                .uri("/api/security/generate-email-verification-token")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class);
    }

    public Mono<Boolean> validateEmailVerificationToken(String token, String username) {
        return webClient.post()
                .uri("/api/security/validate-email-verification-token")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public Mono<TokenResponse> generatePasswordRecoveryToken(@Valid UserDto user) {
        return webClient.post()
                .uri("/api/security/generate-password-recovery-token")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class);
    }

    public Mono<Boolean> validatePasswordRecoveryToken(String token, String username) {
        return webClient.post()
                .uri("/api/security/validate-password-recovery-token")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public Mono<String> invalidateUsedToken(String token) {
        return webClient.post()
                .uri("/api/security/invalidate-used-token")
                .bodyValue(new TokenResponse(token))
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<Boolean> authorizeUser(String token, PermissionEnum permission) {
        return webClient.post()
                .uri("/api/security/authorize")
                .bodyValue(new AuthorizationRequest(token, permission))
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}