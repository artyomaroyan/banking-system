package am.banking.system.transaction.api.controller;

import am.banking.system.transaction.application.port.in.TransactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.08.25
 * Time: 20:59:57
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/transfer")
public class TransactionInternalController {
    private final TransactionUseCase transactionService;

    @PostMapping
    public Mono<Map<String, Object>> create(@RequestBody Map<String, String> payload,
                                            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        UUID transactionId = UUID.fromString(payload.get("transactionId"));
        return transactionService.createTransaction(
                        UUID.fromString(payload.get("userId")),
                        transactionId,
                        payload.get("debitAccount"),
                        payload.get("creditAccount"),
                        payload.get("amount"),
                        idempotencyKey)
                .map(t -> Map.of("transactionId", t.getId().toString(), "status", t.getStatus().name()));
    }
}