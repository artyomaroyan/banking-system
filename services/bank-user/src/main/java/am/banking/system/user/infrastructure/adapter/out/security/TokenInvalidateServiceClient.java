package am.banking.system.user.infrastructure.adapter.out.security;

import am.banking.system.common.shared.response.WebClientResponseHandler;
import am.banking.system.user.application.port.out.JwtTokenServiceClientPort;
import am.banking.system.user.application.port.out.TokenInvalidateClientPort;
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
public class TokenInvalidateServiceClient implements TokenInvalidateClientPort {
    private final WebClient webClient;
    private final JwtTokenServiceClientPort jwtTokenService;
    private final WebClientResponseHandler webClientResponseHandler;

    public TokenInvalidateServiceClient(@Qualifier("securedWebClient") WebClient webClient,
                                        JwtTokenServiceClientPort jwtTokenService,
                                        WebClientResponseHandler webClientResponseHandler) {
        this.webClient = webClient;
        this.jwtTokenService = jwtTokenService;
        this.webClientResponseHandler = webClientResponseHandler;
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<Void> invalidateUsedToken(String token) {
        return jwtTokenService.generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/token/invalidate")
                        .header(AUTHORIZATION, "Bearer " + systemToken)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(Void.class)
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, Void.class, "Token invalidated"))
                        .timeout(Duration.ofSeconds(5)));
    }
}