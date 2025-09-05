package am.banking.system.common.shared.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * Author: Artyom Aroyan
 * Date: 20.08.25
 * Time: 22:43:57
 */
@Validated
public record TransactionRequest(
        @NotBlank(message = "enter debit account number")
        String debitAccount,
        @NotBlank(message = "enter credit account number")
        String creditAccount,
        @NotNull(message = "Please input amount you want to transfer")
        BigDecimal amount) {
}