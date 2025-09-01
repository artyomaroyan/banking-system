package am.banking.system.transaction.api.controller;

import am.banking.system.transaction.application.port.in.TransactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}