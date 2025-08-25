package am.banking.system.transaction.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 24.08.25
 * Time: 20:08:23
 */
@Getter
@AllArgsConstructor
@Table("transaction.account_balance_projection")
public class AccountBalanceProjection {
    private final UUID accountId;
    private final BigDecimal balance;
    private final long version;
    private final Instant updatedAt;
}