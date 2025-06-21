package am.banking.system.account.domain.entity;

import am.banking.system.account.domain.enums.AccountType;
import am.banking.system.common.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 14:37:27
 */
@Table
@Getter
@AllArgsConstructor
public class Account extends BaseEntity {
    private final Integer accountOwnerId;
    private final String accountNumber;
    private final String accountOwnerUsername;
    private final String accountOwnerFullName;
    private final String accountOwnerEmail;
    private final BigDecimal balance;
    private final AccountType accountType;
}