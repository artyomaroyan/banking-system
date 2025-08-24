package am.banking.system.account.domain.entity;

import am.banking.system.common.shared.enums.AccountState;
import am.banking.system.common.shared.enums.Currency;
import am.banking.system.common.shared.enums.AccountType;
import am.banking.system.common.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 14:37:27
 */
@Getter
@AllArgsConstructor
@Table("account.account")
public class Account extends BaseEntity {
    @Column("account_owner_id")
    private final UUID accountOwnerId;
    @Column("account_number")
    private final String accountNumber;
    @Column("account_owner_username")
    private final String accountOwnerUsername;
    @Column("balance")
    private final BigDecimal balance;
    @Column("account_type")
    private final AccountType accountType;
    @Column("currency")
    private final Currency currency;
    @Column("account_state")
    private final AccountState accountState;
    @Version
    @Column("version")
    private final long version;

    public Account withBalance(BigDecimal newBalance) {
        return new Account(
                this.accountOwnerId,
                this.accountNumber,
                this.accountOwnerUsername,
                newBalance,
                this.accountType,
                this.currency,
                this.accountState,
                this.version
        );
    }

    public Account withVersion(long newVersion) {
        return new Account(
                this.accountOwnerId,
                this.accountNumber,
                this.accountOwnerUsername,
                this.balance,
                this.accountType,
                this.currency,
                this.accountState,
                newVersion
        );
    }
}