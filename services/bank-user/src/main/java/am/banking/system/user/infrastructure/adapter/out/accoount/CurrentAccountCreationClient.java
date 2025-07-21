package am.banking.system.user.infrastructure.adapter.out.accoount;

import am.banking.system.common.shared.dto.account.AccountResponse;
import am.banking.system.common.shared.dto.account.AccountCreationRequest;
import am.banking.system.common.shared.response.WebClientResponseHandler;
import am.banking.system.user.application.port.out.UserTokenClientPort;
import am.banking.system.user.application.port.out.account.CurrentAccountCreationClientPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Author: Artyom Aroyan
 * Date: 16.07.25
 * Time: 23:55:18
 */
@Slf4j
@Service
public class CurrentAccountCreationClient implements CurrentAccountCreationClientPort {
    private final WebClient webClient;
    private final UserTokenClientPort userTokenClient;
    private final WebClientResponseHandler webClientResponseHandler;

    public CurrentAccountCreationClient(@Qualifier("securedWebClient") WebClient webClient,
                                        UserTokenClientPort userTokenClient,
                                        WebClientResponseHandler webClientResponseHandler) {
        this.webClient = webClient;
        this.userTokenClient = userTokenClient;
        this.webClientResponseHandler = webClientResponseHandler;
    }

    @Override
    @Retry(name = "accountService")
    @CircuitBreaker(name = "accountService")
    public Mono<AccountResponse> createDefaultAccount(AccountCreationRequest event) {
        return userTokenClient.generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/current-account/default")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + systemToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(event)
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, AccountResponse.class, "Default current account"))
                        .timeout(Duration.ofSeconds(5))
                );
    }
}