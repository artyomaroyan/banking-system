package am.banking.system.account.model.entity;

import am.banking.system.account.model.enums.AccountType;
import am.banking.system.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 14:37:27
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long accountOwnerId;
    private String accountNumber;
    private String accountOwnerUsername;
    private String accountOwnerFullName;
    private String accountOwnerEmail;
    private BigDecimal balance;
    private AccountType accountType;
}