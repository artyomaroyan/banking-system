package am.banking.system.user.infrastructure.security.client;

import am.banking.system.common.dto.security.AuthorizationRequest;
import am.banking.system.common.enums.PermissionEnum;
import am.banking.system.user.infrastructure.security.abstraction.ISecurityServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:33:00
 */
@Service
@RequiredArgsConstructor
public class UserServiceClient implements ISecurityServiceClient {
    private final WebClient webClient;

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Boolean authorizeUser(String token, PermissionEnum permission) {
        return webClient.post()
                .uri("/api/security/authorize")
                .bodyValue(new AuthorizationRequest(token, permission))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}