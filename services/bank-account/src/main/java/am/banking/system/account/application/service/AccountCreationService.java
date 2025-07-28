package am.banking.system.account.application.service;

import am.banking.system.account.api.dto.AccountRequest;
import am.banking.system.common.shared.enums.AccountCurrency;
import am.banking.system.common.shared.dto.account.AccountResponse;
import am.banking.system.account.application.port.in.AccountCreationUseCase;
import am.banking.system.account.domain.entity.Account;
import am.banking.system.account.domain.repository.AccountRepository;
import am.banking.system.common.shared.dto.account.AccountCreationRequest;
import am.banking.system.common.util.GenericMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import static am.banking.system.common.shared.enums.AccountType.CURRENT_ACCOUNT;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 16:37:57
 */
@Service
@RequiredArgsConstructor
public class AccountCreationService implements AccountCreationUseCase {
    private final GenericMapper genericMapper;
    private final AccountRepository accountRepository;

    // TODO: add bank account state to check is account active or freeze. etc.
    @Override
    public Mono<AccountResponse> createDefaultAccount(AccountCreationRequest request) {
        return generateAccountNumber(request.accountCurrency().name())
                .flatMap(accountNumber -> {
                    AccountRequest accountRequest = new AccountRequest(
                            request.userId(),
                            accountNumber,
                            request.username(),
                            BigDecimal.ZERO,
                            CURRENT_ACCOUNT,
                            request.accountCurrency());

                    Account account = genericMapper.map(accountRequest, Account.class);
                    return accountRepository.save(account)
                            .map(savedAccount -> genericMapper.map(savedAccount, AccountResponse.class));
                });
    }

    private Mono<String> generateAccountNumber(String currencyCode) {
        int accPrefix = 12345;
        String accSuffix = AccountCurrency.fromCurrency(currencyCode);

        return Mono.defer(() -> {
            long randomMiddle = ThreadLocalRandom.current().nextLong(100_000_000L, 999_999_999L);
            String accountNumber = "%05d%09d%s".formatted(accPrefix, randomMiddle, accSuffix);

            return accountRepository.existsAccountsByAccountNumber(accountNumber)
                    .flatMap(exists -> Boolean.TRUE.equals(exists) ? generateAccountNumber(currencyCode) : Mono.just(accountNumber));
        });
    }
}