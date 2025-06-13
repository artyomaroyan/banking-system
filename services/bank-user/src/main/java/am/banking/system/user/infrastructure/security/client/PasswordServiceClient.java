package am.banking.system.user.infrastructure.security.client;

import am.banking.system.common.dto.security.PasswordHashingRequest;
import am.banking.system.common.dto.security.PasswordValidatorRequest;
import am.banking.system.user.infrastructure.security.abstraction.IPasswordServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Author: Artyom Aroyan
 * Date: 20.04.25
 * Time: 01:37:13
 */
@Slf4j
@Service
public class PasswordServiceClient implements IPasswordServiceClient {
    private final WebClient webClient;
    private final JwtTokenServiceClient jwtTokenServiceClient;

    public PasswordServiceClient(@Qualifier("securedWebClient") WebClient webClient, JwtTokenServiceClient jwtTokenServiceClient) {
        this.webClient = webClient;
        this.jwtTokenServiceClient = jwtTokenServiceClient;
    }

    @PostConstruct
    void logWebClientType() {
        log.info("Custom Log:: Logging WebClient type: {}", webClient.getClass().getSimpleName());
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<String> hashPassword(String password) {
        return jwtTokenServiceClient.generateSystemToken()
                .flatMap(token -> {
                    log.info("Custom Log:: Generating System Token: {}", token);
                    return  webClient.post()
                            .uri("/api/security/web/hash-password")
                            .header(AUTHORIZATION, "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new PasswordHashingRequest(password))
                            .retrieve()
                            .onStatus(status -> status == HttpStatus.FORBIDDEN,
                                    response -> response.bodyToMono(String.class)
                                            .flatMap(error -> {
                                                log.error("Custom Log:: Forbidden error response body: {}", error);

                                                return Mono.error(new RuntimeException("Forbidden error response body: " + error));
                                            }))
                            .bodyToMono(String.class)
                            .doOnError(error -> log.error("Custom Log:: Unable to hash password: {}", error.getMessage(), error));
                });
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Boolean> validatePassword(String rawPassword, String hashedPassword) {
        return webClient.post()
                .uri("/api/security/web/validate-password")
                .bodyValue(new PasswordValidatorRequest(rawPassword, hashedPassword))
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(error -> log.error("Custom Log:: unable to generate token: {}", error.getMessage(), error));
    }
}