package am.banking.system.user.infrastructure.adapter.out.security;

import am.banking.system.common.shared.dto.security.*;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.common.shared.response.WebClientResponseHandler;
import am.banking.system.user.application.port.out.JwtTokenServiceClientPort;
import am.banking.system.user.application.port.out.UserTokenServiceClientPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:29:29
 */
@Slf4j
@Service
public class UserTokenServiceClient implements UserTokenServiceClientPort {
    private final WebClient webClient;
    private final JwtTokenServiceClientPort jwtTokenServiceClient;
    private final WebClientResponseHandler webClientResponseHandler;

    public UserTokenServiceClient(@Qualifier("securedWebClient") WebClient webClient,
                                  JwtTokenServiceClientPort jwtTokenServiceClient,
                                  WebClientResponseHandler webClientResponseHandler) {
        this.webClient = webClient;
        this.jwtTokenServiceClient = jwtTokenServiceClient;
        this.webClientResponseHandler = webClientResponseHandler;
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<TokenResponse> generateEmailVerificationToken(UserDto user) {
        return jwtTokenServiceClient.generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/user-token/email/issue")
                        .header(AUTHORIZATION, "Bearer " + systemToken)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(user)
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, TokenResponse.class, "Verification token generation"))
                        .timeout(Duration.ofSeconds(5)));
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<TokenValidatorResponse> validateEmailVerificationToken(String token, String username) {
        return jwtTokenServiceClient.generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/user-token/email/validate")
                        .header(AUTHORIZATION, "Bearer " + systemToken)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new TokenValidatorRequest(token, username))
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, TokenValidatorResponse.class, "Verification token validation"))
                        .timeout(Duration.ofSeconds(5)));
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<TokenResponse> generatePasswordRecoveryToken(UserDto user) {
        return jwtTokenServiceClient.generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/user-token/password-reset/issue")
                        .header(AUTHORIZATION, "Bearer " + systemToken)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(user)
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, TokenResponse.class, "Password recovery"))
                        .timeout(Duration.ofSeconds(5)));
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<TokenValidatorResponse> validatePasswordRecoveryToken(String token, String username) {
        return jwtTokenServiceClient.generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/user-token/password-reset/validate")
                        .header(AUTHORIZATION,"Bearer " + systemToken)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new TokenValidatorRequest(token, username))
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, TokenValidatorResponse.class, "Password recovery token validation"))
                        .timeout(Duration.ofSeconds(5)));
    }
}