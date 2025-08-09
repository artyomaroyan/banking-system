package am.banking.system.user.infrastructure.adapter.out.security;

import am.banking.system.common.shared.dto.security.AuthenticationRequest;
import am.banking.system.common.shared.dto.security.AuthenticationResponse;
import am.banking.system.common.shared.response.WebClientResponseHandler;
import am.banking.system.user.application.port.out.AuthenticationClientPort;
import am.banking.system.user.application.port.out.UserTokenClientPort;
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
 * Date: 08.08.25
 * Time: 23:31:51
 */
@Slf4j
@Service
public class AuthenticationClient implements AuthenticationClientPort {
    private final WebClient webClient;
    private final UserTokenClientPort userTokenClient;
    private final WebClientResponseHandler webClientResponseHandler;

    public AuthenticationClient(@Qualifier("securityWebClient") WebClient webClient,
                                UserTokenClientPort userTokenClient,
                                WebClientResponseHandler webClientResponseHandler) {
        this.webClient = webClient;
        this.userTokenClient = userTokenClient;
        this.webClientResponseHandler = webClientResponseHandler;
    }

    @Override
    public Mono<AuthenticationResponse> authenticate(String username, String password) {
        return userTokenClient.generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/internal/security/authenticate")
                        .header(AUTHORIZATION, "Bearer " + systemToken)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(new AuthenticationRequest(username, password))
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, AuthenticationResponse.class, "Authentication"))
                        .timeout(Duration.ofSeconds(5))
                        .doOnNext(_ -> log.info("AuthenticationClient: user '{}' authenticated successfully", username))
                        .doOnError(err -> log.error("AuthenticationClient: error for '{}' : {}", username,  err.getMessage()))
                );
    }
}