package am.banking.system.transaction.domain.model;

import am.banking.system.common.shared.model.BaseEntity;
import am.banking.system.transaction.domain.enums.TransactionStatus;
import am.banking.system.transaction.domain.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 20.08.25
 * Time: 22:34:02
 */
@Getter
@Setter
@Builder(toBuilder = true)
@Table("transaction.transaction")
public class Transaction extends BaseEntity {
    private UUID userId;
    private String debitAccount;
    private String creditAccount;
    private BigDecimal amount;
    private TransactionStatus status;
    private String reservationId;
    private String idempotencyKey;
    private TransactionType type;
}
