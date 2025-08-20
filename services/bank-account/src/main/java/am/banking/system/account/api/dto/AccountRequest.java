package am.banking.system.account.api.dto;

import am.banking.system.common.shared.enums.Currency;
import am.banking.system.common.shared.enums.AccountType;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 16:10:33
 */
@Validated
public record AccountRequest(
        @NotBlank(message = "account owner ID must not be blank")
        UUID accountOwnerId,
        @NotBlank(message = "Account number must not be blank")
        @Pattern(regexp = "\\d{16}", message = "Account number must be exactly 16 digits")
        String accountNumber,
        @NotBlank(message = "account owner username is required")
        String accountOwnerUsername,
        @NotNull(message = "Balance is required")
        @DecimalMin(value = "0.00", message = "Balance must be zero or positive")
        @Digits(integer = 10, fraction = 2, message = "Balance must be a valid monetary amount")
        BigDecimal balance,
        @NotNull(message = "Account type is required")
        AccountType accountType,
        @NotNull(message = "Account currency is required")
        Currency currency){
}