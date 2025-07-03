package am.banking.system.user.infrastructure.adapter.out.security;

import am.banking.system.common.shared.dto.security.PasswordHashingRequest;
import am.banking.system.common.shared.dto.security.PasswordHashingResponse;
import am.banking.system.common.shared.dto.security.PasswordValidatorRequest;
import am.banking.system.user.application.port.out.JwtTokenServiceClientPort;
import am.banking.system.user.application.port.out.PasswordServiceClientPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
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
public class PasswordServiceClient implements PasswordServiceClientPort {
    private final WebClient webClient;
    private final JwtTokenServiceClientPort jwtTokenServiceClient;

    public PasswordServiceClient(@Qualifier("securedWebClient") WebClient webClient, JwtTokenServiceClient jwtTokenServiceClient) {
        this.webClient = webClient;
        this.jwtTokenServiceClient = jwtTokenServiceClient;
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<PasswordHashingResponse> hashPassword(String password) {
        return jwtTokenServiceClient.generateSystemToken()
                .flatMap(token -> {
                    log.info("Generating System Token: {}", token);
                    return webClient.post()
                            .uri("/api/internal/security/password/hash")
                            .header(AUTHORIZATION, "Bearer " + token)
                            .contentType(APPLICATION_JSON)
                            .bodyValue(new PasswordHashingRequest(password))
                            .exchangeToMono(response -> {
                                HttpStatusCode status = response.statusCode();

                                if (status.is2xxSuccessful()) {
                                    return response.bodyToMono(PasswordHashingResponse.class)
                                            .doOnNext(body -> log.info(
                                                    "Password hashing response: {}", body.hashedPassword()))
                                            .switchIfEmpty(Mono.error(new RuntimeException("Password hash returned empty body")));
                                } else {
                                    return response.bodyToMono(PasswordHashingResponse.class)
                                            .switchIfEmpty(Mono.error(new RuntimeException("Password hash returned empty body")))
                                            .flatMap(error -> {
                                                log.error("Password hashing failed - status: {}, body: {}", status.value(), error);
                                                return  Mono.error(new RuntimeException(
                                                        "Password hashing failed with status: " + status.value() + " - " + error));
                                            });
                                }
                            })
                            .timeout(Duration.ofSeconds(5))
                            .doOnError(error -> log.error("Password hashing failed: {}", error.getMessage(), error));
                });
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Boolean> validatePassword(String rawPassword, String hashedPassword) {
        return webClient.post()
                .uri("/api/internal/security/password/validate")
                .bodyValue(new PasswordValidatorRequest(rawPassword, hashedPassword))
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }
}