package am.banking.system.account.service;

import am.banking.system.account.model.dto.AccountRequest;
import am.banking.system.account.model.dto.AccountResponse;
import am.banking.system.account.model.entity.Account;
import am.banking.system.account.model.repository.AccountRepository;
import am.banking.system.common.dto.user.UserRegistrationEvent;
import am.banking.system.common.mapper.GenericMapper;
import am.banking.system.common.reponse.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static am.banking.system.account.model.enums.AccountType.CURRENT_ACCOUNT;

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
    public Result<AccountResponse> createDefaultAccount(AccountRequest request) {
        return null;
    }

    private Mono<AccountResponse> createAccount(UserRegistrationEvent event) {
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