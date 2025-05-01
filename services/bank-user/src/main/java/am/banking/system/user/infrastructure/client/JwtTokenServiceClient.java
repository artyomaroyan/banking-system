package am.banking.system.user.infrastructure.client;

import am.banking.system.common.dto.TokenResponse;
import am.banking.system.common.dto.TokenValidatorRequest;
import am.banking.system.common.dto.UserDto;
import am.banking.system.user.infrastructure.abstraction.IJwtTokenServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:26:27
 */
@Service
@RequiredArgsConstructor
public class JwtTokenServiceClient implements IJwtTokenServiceClient {
    private final WebClient webClient;

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public TokenResponse generateJwtToken(@Valid UserDto user) {
        return webClient.post()
                .uri("/api/security/generate-jwt-token")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Boolean validateJwtToken(String token, String username) {
        return webClient.post()
                .uri("/api/security/validate-jwt-token")
                .bodyValue(new TokenValidatorRequest(token, username))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}