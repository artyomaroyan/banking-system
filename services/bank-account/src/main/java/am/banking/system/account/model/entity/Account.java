package am.banking.system.account.model.entity;

import am.banking.system.account.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 14:37:27
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String accountOwnerId;
    private String accountNumber;
    private String accountOwnerUsername;
    private String accountOwnerFullName;
    private String accountOwnerEmail;
    private BigDecimal balance;
    private AccountType accountType;
    private LocalDate createdAt;
}