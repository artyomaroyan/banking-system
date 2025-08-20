package am.banking.system.account.domain.entity;

import am.banking.system.common.shared.enums.Currency;
import am.banking.system.common.shared.enums.AccountType;
import am.banking.system.common.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
    private Currency currency;
}