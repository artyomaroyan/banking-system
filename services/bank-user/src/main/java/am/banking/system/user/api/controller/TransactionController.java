package am.banking.system.user.api.controller;

import am.banking.system.common.shared.dto.transaction.TransactionRequest;
import am.banking.system.user.application.port.in.transaction.TransactionUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 07.09.25
 * Time: 22:21:53
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfer")
public class TransactionController {
    private final TransactionUseCase transactionService;

    @PostMapping("/create")
    ResponseEntity<Mono<Map<String, String>>> create(@Valid @RequestBody TransactionRequest request,
                                                     @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        var result = transactionService.makeTransfer(request, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}