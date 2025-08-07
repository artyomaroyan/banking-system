package am.banking.system.common.shared.dto.account;


import am.banking.system.common.shared.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 16:12:48
 */
public record AccountResponse(UUID accountOwnerId, String accountNumber, String accountOwnerUsername,
                              BigDecimal balance, AccountType accountType, LocalDateTime createdAt) {
}