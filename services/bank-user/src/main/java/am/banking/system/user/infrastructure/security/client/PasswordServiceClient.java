package am.banking.system.user.infrastructure.security.client;

import am.banking.system.common.dto.security.PasswordHashingRequest;
import am.banking.system.common.dto.security.PasswordValidatorRequest;
import am.banking.system.user.infrastructure.security.abstraction.IPasswordServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
                    log.info("Custom Log:: Generated system token: {}", token);
                    return webClient.post()
                            .uri("/api/security/web/hash-password")
                            .header(AUTHORIZATION, "Bearer " + token)
                            .bodyValue(new PasswordHashingRequest(password))
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnError(error -> log.error("Custom Log:: unable to hash password: {}", error.getMessage(), error));
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