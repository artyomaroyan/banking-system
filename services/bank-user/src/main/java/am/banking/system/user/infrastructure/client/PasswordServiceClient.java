package am.banking.system.user.infrastructure.client;

import am.banking.system.common.dto.PasswordHashingRequest;
import am.banking.system.common.dto.PasswordValidatorRequest;
import am.banking.system.user.infrastructure.abstraction.IPasswordServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Author: Artyom Aroyan
 * Date: 20.04.25
 * Time: 01:37:13
 */
@Service
@RequiredArgsConstructor
public class PasswordServiceClient implements IPasswordServiceClient {
    private final WebClient webClient;

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public String hashPassword(String password) {
        return webClient.post()
                .uri("/api/security/hash-password")
                .bodyValue(new PasswordHashingRequest(password))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Boolean validatePassword(String rawPassword, String hashedPassword) {
        return webClient.post()
                .uri("/api/security/validate-password")
                .bodyValue(new PasswordValidatorRequest(rawPassword, hashedPassword))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}