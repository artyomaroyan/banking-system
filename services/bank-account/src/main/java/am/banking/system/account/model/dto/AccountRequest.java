package am.banking.system.account.model.dto;

import am.banking.system.account.model.enums.AccountType;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 16:10:33
 */
@Validated
public record AccountRequest(
        @NotBlank(message = "account owner ID must not be blank")
        String accountOwnerId,
        @NotBlank(message = "Account number must not be blank")
//        @Size(min = 16, max = 16, message = "Account number must be exactly 16 digits")
        @Pattern(regexp = "\\d{16}", message = "Account number must be exactly 16 digits")
        String accountNumber,
        @NotBlank(message = "account owner username is required")
        String accountOwnerUsername,
        @NotBlank(message = "account owner full name is required")
        String accountOwnerFullName,
        @Email
        @NotBlank(message = "account owner email is required")
        String accountOwnerEmail,
        @NotNull(message = "Balance is required")
        @DecimalMin(value = "0.00", message = "Balance must be zero or positive")
        @Digits(integer = 10, fraction = 2, message = "Balance must be a valid monetary amount")
        BigDecimal balance,
        @NotNull(message = "Account type is required")
        AccountType accountType,
        @NotNull(message = "Creation date is required")
        @PastOrPresent(message = "Creation date cannot be in the future")
        LocalDate createdAt){
}