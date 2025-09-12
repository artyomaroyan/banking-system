package am.banking.system.common.shared.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 20.08.25
 * Time: 22:43:57
 */
@Validated
public record TransactionRequest(
        @NotNull UUID userId,
        @NotNull UUID transactionId,
        @NotBlank String from,
        @NotBlank String to,
        @NotNull @Positive BigDecimal amount,
        @NotNull String idempotencyKey) {
}