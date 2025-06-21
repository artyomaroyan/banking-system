package am.banking.system.user.infrastructure.client.security;

import am.banking.system.common.shared.dto.security.AuthorizationRequest;
import am.banking.system.common.shared.enums.PermissionEnum;
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
 * Time: 00:33:00
 */
@Slf4j
@Service
public class UserServiceClient implements ISecurityServiceClient {
    private final WebClient webClient;

    public UserServiceClient(@Qualifier("securedWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @PostConstruct
    void logWebClientType() {
        log.info("Custom Log:: Logging WebClient type: {}", webClient.getClass().getSimpleName());
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Boolean> authorizeUser(String token, PermissionEnum permission) {
        return webClient.post()
                .uri("/api/security/web/authorize")
                .bodyValue(new AuthorizationRequest(token, permission))
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }
}