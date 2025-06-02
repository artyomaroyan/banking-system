package am.banking.system.account.model.dto;

import am.banking.system.account.model.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 16:12:48
 */
public record AccountResponse(String accountOwnerId, String accountNumber,
                              String accountOwnerUsername, String accountOwnerFullName,
                              BigDecimal balance, AccountType accountType, LocalDate createdAt) {
}