package am.banking.system.transaction.api.dto;

import am.banking.system.transaction.domain.enums.TransactionStatus;
import am.banking.system.transaction.domain.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 04.09.25
 * Time: 22:36:46
 */
@Validated
public record TransactionDto(
        @NotNull UUID transactionId,
        @NotNull UUID userId,
        @NotBlank String debitAccount,
        @NotBlank String creditAccount,
        @NotNull @Positive BigDecimal amount,
        @NotNull TransactionStatus status,
        String reservationId,
        TransactionType transactionType) {

    public TransactionDto withStatus(TransactionStatus status) {
        return new TransactionDto(transactionId, userId, debitAccount, creditAccount, amount, status, reservationId, transactionType);
    }

    public TransactionDto withReservation(String reservationId) {
        return new TransactionDto(transactionId,userId,debitAccount, creditAccount, amount, TransactionStatus.RESERVED, reservationId, transactionType);
    }
}