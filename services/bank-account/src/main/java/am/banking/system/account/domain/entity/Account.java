package am.banking.system.account.domain.entity;

import am.banking.system.common.shared.enums.AccountCurrency;
import am.banking.system.common.shared.enums.AccountType;
import am.banking.system.common.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

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
    private final Integer accountOwnerId;
    @Column("account_number")
    private final String accountNumber;
    @Column("account_owner_username")
    private final String accountOwnerUsername;
    @Column("balance")
    private final BigDecimal balance;
    @Column("account_type")
    private final AccountType accountType;
    @Column("account_currency")
    private AccountCurrency accountCurrency;
}