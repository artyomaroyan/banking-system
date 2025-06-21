package am.banking.system.account.application.service;

import am.banking.system.account.api.dto.AccountRequest;
import am.banking.system.account.api.dto.AccountResponse;
import am.banking.system.account.domain.entity.Account;
import am.banking.system.account.domain.repository.AccountRepository;
import am.banking.system.common.shared.dto.user.UserRegistrationEvent;
import am.banking.system.common.util.GenericMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static am.banking.system.account.domain.enums.AccountType.CURRENT_ACCOUNT;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 16:37:57
 */
@Service
@RequiredArgsConstructor
public class AccountCreationService implements IAccountCreationService {
    private final GenericMapper genericMapper;
    private final DatabaseClient databaseClient;
    private final AccountRepository accountRepository;

    @Override
    public Mono<AccountResponse> createDefaultAccount(UserRegistrationEvent event) {
        return generateAccountNumber()
                .flatMap(accountNumber -> {
                    AccountRequest accountRequest = new AccountRequest(
                            event.userId(),
                            accountNumber,
                            event.username(),
                            event.firstName() + event.lastName(),
                            event.email(),
                            BigDecimal.valueOf(0.00),
                            CURRENT_ACCOUNT,
                            LocalDate.now());

                    Account account = genericMapper.map(accountRequest, Account.class);
                    return accountRepository.save(account)
                            .map(savedAccount -> genericMapper.map(savedAccount, AccountResponse.class));
                });
    }

    private Mono<String> generateAccountNumber() {
        int bankCode = 12345;
        return databaseClient.sql("SELECT nextval('account_number_seq') as seq")
                .map(row -> row.get("seq", Long.class))
                .first()
                .handle((seq, sink) -> {
                    if (seq == null) {
                        sink.error(new IllegalStateException("Failed to generate account number"));
                        return;
                    }
                    sink.next("%05d%011d".formatted(bankCode, seq));
                });
    }
}