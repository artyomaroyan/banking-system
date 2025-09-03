package am.banking.system.transaction.domain.model;

import am.banking.system.common.shared.model.BaseEntity;
import am.banking.system.transaction.domain.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 20.08.25
 * Time: 22:34:02
 */
@Getter
@Builder
@Table("transaction.transaction")
public final class Transaction extends BaseEntity {
    private final UUID userId;
    private final String debitAccount;
    private final String creditAccount;
    private final BigDecimal amount;
    private final String status; // ToDo: make status Enum
    private final String reservationId;
    private final String idempotencyKey;
    private final TransactionType transactionType;
}
