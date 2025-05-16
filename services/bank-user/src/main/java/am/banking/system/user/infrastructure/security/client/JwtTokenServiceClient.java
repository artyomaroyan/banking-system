package am.banking.system.user.infrastructure.security.client;

import am.banking.system.common.dto.UserDto;
import am.banking.system.common.dto.security.TokenResponse;
import am.banking.system.common.dto.security.TokenValidatorRequest;
import am.banking.system.user.infrastructure.security.abstraction.IJwtTokenServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:26:27
 */
@Slf4j
@Service
public class JwtTokenServiceClient implements IJwtTokenServiceClient {
    private final WebClient webClient;

    public JwtTokenServiceClient(@Qualifier("securedWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @PostConstruct
    void logWebClientType() {
        log.info("Logging WebClient type: {}", webClient.getClass().getSimpleName());
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<TokenResponse> generateJwtToken(@Valid UserDto user) {
        return webClient.post()
                .uri("/api/security/web/generate-jwt-token")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class);
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<String> generateSystemToken() {
        return webClient.post()
                .uri("/api/v1/secure/local/system-token")
                .retrieve()
                .bodyToMono(String.class);
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Mono<Boolean> validateJwtToken(String token, String username) {
        return webClient.post()
                .uri("/api/security/web/validate-jwt-token")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}