package am.banking.system.common.messages;

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
 * Time: 16:32:55
 */
@Validated
public record ValidateAndReserveCommand(
        @NotNull UUID transferId,
        @NotBlank String fromAccount,
        @NotNull @Positive BigDecimal amount,
        @NotNull Instant requestedAt) {
}