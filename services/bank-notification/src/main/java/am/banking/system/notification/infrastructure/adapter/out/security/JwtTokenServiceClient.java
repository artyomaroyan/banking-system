package am.banking.system.notification.infrastructure.adapter.out.security;

import am.banking.system.common.infrastructure.tls.configuration.InternalSecretProperties;
import am.banking.system.common.shared.exception.security.token.EmptyTokenException;
import am.banking.system.common.shared.exception.security.TimeoutException;
import am.banking.system.common.shared.exception.security.token.TokenGenerationException;
import am.banking.system.notification.application.port.out.JwtTokenServiceClientPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Author: Artyom Aroyan
 * Date: 27.06.25
 * Time: 01:28:35
 */
@Slf4j
@Service
public class JwtTokenServiceClient implements JwtTokenServiceClientPort {
    private final WebClient webClient;
    private final InternalSecretProperties secretProperties;

    public JwtTokenServiceClient(@Qualifier("securedWebClient") WebClient webClient, InternalSecretProperties secretProperties) {
        this.webClient = webClient;
        this.secretProperties = secretProperties;
    }

    @Override
    public Mono<String> generateSystemToken() {
        log.info("Sending notification request with internal secret: {}", secretProperties.secret());
        return webClient.get()
                .uri("/api/v1/secure/local/system-token")
                .headers(header -> header.set("X-Internal-Secret", secretProperties.secret()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    log.error("Failed to get system token. Status: {}", response.statusCode());

                    return response.bodyToMono(String.class)
                            .defaultIfEmpty("No error body")
                            .flatMap(body -> Mono.error(new TokenGenerationException(
                                    "System token request failed: " + response.statusCode() + " - " +  body
                            )));
                })
                .bodyToMono(String.class)
                .doOnNext(token -> {
                    if (token == null || token.isEmpty() || token.trim().isBlank()) {
                        log.error("No token provided.");
                        throw new EmptyTokenException("No token provided.");
                    }
                    log.info("Received system token: {}", token);
                })
                .timeout(Duration.ofSeconds(10),
                        Mono.error(new TimeoutException("System token request timed out.")));
    }
}