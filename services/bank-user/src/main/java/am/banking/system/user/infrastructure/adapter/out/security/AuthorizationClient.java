package am.banking.system.user.infrastructure.adapter.out.security;

import am.banking.system.common.shared.dto.security.AuthorizationRequest;
import am.banking.system.common.shared.enums.PermissionEnum;
import am.banking.system.common.shared.response.WebClientResponseHandler;
import am.banking.system.user.application.port.out.AuthorizationClientPort;
import am.banking.system.user.application.port.out.UserTokenClientPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:33:00
 */
@Slf4j
@Service
public class AuthorizationClient implements AuthorizationClientPort {
    private final WebClient webClient;
    private final UserTokenClientPort userTokenClient;
    private final WebClientResponseHandler webClientResponseHandler;

    public AuthorizationClient(@Qualifier("securityWebClient") WebClient webClient,
                               UserTokenClientPort jwtTokenServiceClient,
                               WebClientResponseHandler webClientResponseHandler) {
        this.webClient = webClient;
        this.userTokenClient = jwtTokenServiceClient;
        this.webClientResponseHandler = webClientResponseHandler;
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<Boolean> authorizeUser(String token, PermissionEnum permission) {
        return userTokenClient.generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/authorize")
                        .header(AUTHORIZATION, "Bearer " + systemToken)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new AuthorizationRequest(token, permission))
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, Boolean.class, "Authorization"))
                        .timeout(Duration.ofSeconds(5)));

    }
}