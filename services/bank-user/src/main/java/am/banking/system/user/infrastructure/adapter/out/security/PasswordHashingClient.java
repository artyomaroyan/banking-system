package am.banking.system.user.infrastructure.adapter.out.security;

import am.banking.system.common.shared.dto.security.PasswordHashingRequest;
import am.banking.system.common.shared.dto.security.PasswordHashingResponse;
import am.banking.system.common.shared.response.WebClientResponseHandler;
import am.banking.system.user.application.port.out.security.PasswordHashingClientPort;
import am.banking.system.user.application.port.out.security.UserTokenClientPort;
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
 * Date: 20.04.25
 * Time: 01:37:13
 */
@Slf4j
@Service
public class PasswordHashingClient implements PasswordHashingClientPort {
    private final WebClient webClient;
    private final UserTokenClientPort userTokenClient;
    private final WebClientResponseHandler webClientResponseHandler;

    public PasswordHashingClient(@Qualifier("securityWebClient") WebClient webClient,
                                 UserTokenClientPort jwtTokenServiceClient, WebClientResponseHandler webClientResponseHandler) {
        this.webClient = webClient;
        this.userTokenClient = jwtTokenServiceClient;
        this.webClientResponseHandler = webClientResponseHandler;
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<PasswordHashingResponse> hashPassword(String password) {
        return userTokenClient.generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/password/hash")
                        .header(AUTHORIZATION, "Bearer " + systemToken)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new PasswordHashingRequest(password))
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, PasswordHashingResponse.class, "Password Hashing"))
                        .timeout(Duration.ofSeconds(5)));
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<Boolean> validatePassword(String rawPassword, String hashedPassword) {
        return userTokenClient.generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/password/validate")
                        .header(AUTHORIZATION, "Bearer " + systemToken)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(Boolean.class)
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, Boolean.class, "Password Validation"))
                        .timeout(Duration.ofSeconds(5)));
    }
}