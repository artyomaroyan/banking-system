package am.banking.system.user.infrastructure.adapter.out.security;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.security.TokenValidatorRequest;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.common.shared.exception.security.EmptyTokenException;
import am.banking.system.user.application.port.out.JwtTokenServiceClientPort;
import am.banking.system.user.application.port.out.UserTokenServiceClientPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
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

    public UserTokenServiceClient(@Qualifier("securedWebClient") WebClient webClient, JwtTokenServiceClientPort jwtTokenServiceClient) {
        this.webClient = webClient;
        this.jwtTokenServiceClient = jwtTokenServiceClient;
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<TokenResponse> generateEmailVerificationToken(UserDto user) {
        return jwtTokenServiceClient.generateSystemToken()
                .switchIfEmpty(Mono.error(new EmptyTokenException("System token generation returned empty")))
                .flatMap(token ->
                        webClient.post()
                                .uri("/api/internal/security/user-token/email/issue")
                                .header(AUTHORIZATION, "Bearer " + token)
                                .contentType(APPLICATION_JSON)
                                .bodyValue(user)
                                .exchangeToMono(response -> {
                                    HttpStatusCode statusCode = response.statusCode();

                                    if (statusCode.is2xxSuccessful()) {
                                        return response.bodyToMono(TokenResponse.class)
//                                                .doOnNext(body -> log.info("Email verification response: {}", body.token()))
                                                .switchIfEmpty(Mono.error(new EmptyTokenException("Email verification returned empty body")));
                                    } else {
                                        return response.createException()
                                                .flatMap(error -> {log.error(
                                                        "Email verification token generation failed - status: {}, body: {}",
                                                            statusCode.value(), error.getResponseBodyAsString());
                                                    return Mono.error(new EmptyTokenException(
                                                            "Email verification token generation failed - " + error.getMessage()));
                                                });
                                    }
                                })
                                .timeout(Duration.ofSeconds(10))
                                .doOnError(error -> log.error(
                                        "Email verification token generation failed: {}", error.getMessage(), error)));
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Boolean> validateEmailVerificationToken(String token, String username) {
        return webClient.post()
                .uri("/api/internal/security/user-token/email/validate")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<TokenResponse> generatePasswordRecoveryToken(UserDto user) {
        return webClient.post()
                .uri("/api/internal/security/user-token/password-reset/issue")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Boolean> validatePasswordRecoveryToken(String token, String username) {
        return webClient.post()
                .uri("/api/internal/security/user-token/password-reset/validate")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Void> invalidateUsedToken(String token) {
        return webClient.post()
                .uri("/api/internal/security/token/invalidate")
                .bodyValue(new TokenResponse(token))
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }
}