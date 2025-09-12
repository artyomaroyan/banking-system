package am.banking.system.transaction.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 12.09.25
 * Time: 12:41:26
 */
@Validated
public record CreateTransferRequest(
        @NotNull UUID transactionId,
        @NotNull UUID userId,
        @NotNull String from,
        @NotNull String to,
        @NotNull @Positive BigDecimal amount) {
}