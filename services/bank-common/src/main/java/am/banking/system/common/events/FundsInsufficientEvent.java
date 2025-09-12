package am.banking.system.common.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 31.08.25
 * Time: 16:37:04
 */
@Validated
public record FundsInsufficientEvent(
        @NotNull UUID transferId,
        @NotBlank String account,
        @NotNull @Positive BigDecimal amount,
        @NotNull String reason,
        @NotNull Instant occurredAt) {
}