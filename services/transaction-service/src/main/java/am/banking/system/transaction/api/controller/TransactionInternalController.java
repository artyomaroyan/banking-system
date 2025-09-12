package am.banking.system.transaction.api.controller;

import am.banking.system.transaction.api.dto.CreateTransferRequest;
import am.banking.system.transaction.application.port.in.TransactionUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

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

    @PostMapping("/create")
    public Mono<Map<String, String>> create(@Valid @RequestBody CreateTransferRequest request,
                                            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        return transactionService.createTransaction(
                request.userId(),
                request.transactionId(),
                request.from(),
                request.to(),
                request.amount(),
                idempotencyKey)
                .map(t -> Map.of("transactionId", t.getId().toString(), "status", t.getStatus().name()));
    }
}