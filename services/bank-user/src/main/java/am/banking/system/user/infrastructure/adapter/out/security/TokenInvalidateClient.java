package am.banking.system.user.infrastructure.adapter.out.security;

import am.banking.system.common.shared.response.WebClientResponseHandler;
import am.banking.system.user.application.port.out.TokenInvalidateClientPort;
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
 * Date: 06.07.25
 * Time: 15:31:53
 */
@Slf4j
@Service
public class TokenInvalidateClient implements TokenInvalidateClientPort {
    private final WebClient webClient;
    private final UserTokenClientPort userTokenClient;
    private final WebClientResponseHandler webClientResponseHandler;

    public TokenInvalidateClient(@Qualifier("securedWebClient") WebClient webClient,
                                 UserTokenClientPort userTokenClient,
                                 WebClientResponseHandler webClientResponseHandler) {
        this.webClient = webClient;
        this.userTokenClient = userTokenClient;
        this.webClientResponseHandler = webClientResponseHandler;
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<String> invalidateUsedToken(String token) {
        return userTokenClient.generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/token/invalidate")
                        .header(AUTHORIZATION, "Bearer " + systemToken)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(Void.class)
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, String.class, "Token invalidated"))
                        .timeout(Duration.ofSeconds(5)));
    }
}