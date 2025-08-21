package am.banking.system.payment.api.dto;

import am.banking.system.payment.domain.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * Author: Artyom Aroyan
 * Date: 20.08.25
 * Time: 02:20:54
 */
@Validated
public record PaymentRequest(
        @NotNull(message = "Please select payment type")
        PaymentType paymentType,
        String debitAccount,
        BigDecimal amount
) {
}