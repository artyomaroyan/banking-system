package am.banking.system.transaction.api.dto;

import am.banking.system.transaction.domain.enums.TransactionType;
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
        @NotNull(message = "Please select transaction type")
        TransactionType transactionType, // ToDo: if selected type is card then popup window should require card number if type is account then should require account umber.
        @NotBlank(message = "enter required number")
        String accountNumber,
        @NotNull(message = "Please input amount you want to transfer")
        BigDecimal amount
) {
}