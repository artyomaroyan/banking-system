package am.banking.system.user.infrastructure.adapter.out.security;

import am.banking.system.common.infrastructure.configuration.InternalSecretProperties;
import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.security.TokenValidatorRequest;
import am.banking.system.common.shared.dto.security.TokenValidatorResponse;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.common.shared.exception.security.TimeoutException;
import am.banking.system.common.shared.exception.security.token.EmptyTokenException;
import am.banking.system.common.shared.exception.security.token.TokenGenerationException;
import am.banking.system.common.shared.response.WebClientResponseHandler;
import am.banking.system.user.application.port.out.UserTokenClientPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
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
public class UserTokenClient implements UserTokenClientPort {
    private final WebClient webClient;
    private final InternalSecretProperties secretProperties;
    private final WebClientResponseHandler webClientResponseHandler;

    public UserTokenClient(@Qualifier("securityWebClient") WebClient webClient,
                           InternalSecretProperties secretProperties,
                           WebClientResponseHandler webClientResponseHandler) {
        this.webClient = webClient;
        this.secretProperties = secretProperties;
        this.webClientResponseHandler = webClientResponseHandler;
    }

    @Override
    public Mono<String> generateSystemToken() {
        return webClient.get()
                .uri("/api/v1/secure/local/system-token")
                .header("X-Internal-Secret", secretProperties.secret())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("No error body")
                                .flatMap(body -> {
                                    log.error("System token failed - status: {}, body: {}", response.statusCode(), body);
                                    return Mono.error(new TokenGenerationException(
                                            "System token request failed: " + response.statusCode() + " - " + body));
                                }))
                .bodyToMono(String.class)
                .doOnNext(token -> {
                    if (token == null || token.isBlank()) {
                        throw new EmptyTokenException("Received empty system token");
                    }
                    log.info("System token successfully generated");
                })
                .timeout(Duration.ofSeconds(5),
                        Mono.error(new TimeoutException("System token request timed out")));
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<TokenResponse> generateJwtAccessToken(UserDto user) {
        return generateSystemToken()
                .flatMap(token -> webClient.post()
                        .uri("/api/internal/security/token/access/issue")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(user)
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, TokenResponse.class, "JWT Generation"))
                        .timeout(Duration.ofSeconds(5)));
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<TokenResponse> generateEmailVerificationToken(UserDto user) {
        return generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/token/email/issue")
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
    public Mono<TokenResponse> generatePasswordRecoveryToken(UserDto user) {
        return generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/token/password-reset/issue")
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
    public Mono<TokenValidatorResponse> validateJwtAccessToken(@NotNull Integer userId, String username, String token) {
        return generateSystemToken()
                .flatMap(_ -> webClient.post()
                        .uri("/api/internal/security/token/access/validate")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new TokenValidatorRequest(userId, username,  token))
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, TokenValidatorResponse.class, "JWT Validation"))
                        .timeout(Duration.ofSeconds(5)));
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<TokenValidatorResponse> validatePasswordRecoveryToken(@NotNull Integer userId, String username, String token) {
        return generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/token/password-reset/validate")
                        .header(AUTHORIZATION, "Bearer " + systemToken)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new TokenValidatorRequest(userId, username, token))
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, TokenValidatorResponse.class, "Password recovery token validation"))
                        .timeout(Duration.ofSeconds(5)));
    }

    @Override
    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    public Mono<TokenValidatorResponse> validateEmailVerificationToken(@NotNull Integer userId, @NotBlank String username, @NotBlank String token) {
        return generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/token/email/validate")
                        .header(AUTHORIZATION, "Bearer " + systemToken)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new TokenValidatorRequest(userId, username, token))
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, TokenValidatorResponse.class, "Email verification token validation"))
                        .timeout(Duration.ofSeconds(5)));
    }
}