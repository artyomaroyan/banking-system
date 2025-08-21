package am.banking.system.payment.domain.entity;

import am.banking.system.common.shared.enums.Currency;
import am.banking.system.common.shared.model.BaseEntity;
import am.banking.system.payment.domain.enums.PaymentType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 19.08.25
 * Time: 22:56:59
 */
@Getter
@Builder
@Table("payment.payment")
public final class Payment extends BaseEntity {
    private final UUID userId;
    private final String username;
    private final BigDecimal amount;
    private final String debitAccount;
    private final Currency currency;
    private final String description;
    private final PaymentType paymentType;
}