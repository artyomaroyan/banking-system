package am.banking.system.user.infrastructure.adapter.out.transaction;

import am.banking.system.common.shared.dto.transaction.TransactionRequest;
import am.banking.system.common.shared.response.WebClientResponseHandler;
import am.banking.system.user.application.port.out.security.UserTokenClientPort;
import am.banking.system.user.application.port.out.transaction.TransactionClientPort;
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
import java.util.Map;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 31.08.25
 * Time: 16:49:16
 */
@Slf4j
@Service
public class TransactionClient implements TransactionClientPort {
    private final WebClient webClient;
    private final UserTokenClientPort userTokenClient;
    private final WebClientResponseHandler webClientResponseHandler;

    public TransactionClient(@Qualifier("transactionWebClient") WebClient webClient,
                             UserTokenClientPort userTokenClient, WebClientResponseHandler webClientResponseHandler) {
        this.webClient = webClient;
        this.userTokenClient = userTokenClient;
        this.webClientResponseHandler = webClientResponseHandler;
    }

    @Override
    @Retry(name = "transactionService")
    @CircuitBreaker(name = "transactionService")
    public Mono<Map<String, String>> createTransfer(TransactionRequest request, String idempotencyKey) {
        UUID transactionId = UUID.randomUUID();
        return userTokenClient.generateSystemToken()
                .flatMap(systemToken -> webClient.post()
                        .uri("/api/v1/internal/transfer/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + systemToken)
                        .header("Idempotency-Key", idempotencyKey == null ? transactionId.toString() : idempotencyKey)
                        .bodyValue(Map.of(
                                "transferId", transactionId,
                                "from", request.from(),
                                "to", request.to(),
                                "amount", request.amount()
                                ))
                        .exchangeToMono(response -> webClientResponseHandler
                                .response(response, Map.class, "createTransfer")
                                .map(raw -> (Map<String, String>) raw))
                        .timeout(Duration.ofSeconds(5))
                );
    }
}