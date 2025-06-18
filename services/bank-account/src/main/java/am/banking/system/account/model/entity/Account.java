package am.banking.system.account.model.entity;

import am.banking.system.account.model.enums.AccountType;
import am.banking.system.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 14:37:27
 */
@Table
@Getter
@AllArgsConstructor
public class Account extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer accountOwnerId;
    private final String accountNumber;
    private final String accountOwnerUsername;
    private final String accountOwnerFullName;
    private final String accountOwnerEmail;
    private final BigDecimal balance;
    private final AccountType accountType;
}