package am.banking.system.account.application.service;

import am.banking.system.account.application.port.in.AccountServiceUseCase;
import am.banking.system.account.domain.entity.Account;
import am.banking.system.account.domain.repository.AccountRepository;
import am.banking.system.account.outbox.OutboxEvent;
import am.banking.system.account.outbox.OutboxRepository;
import am.banking.system.common.contracts.event.AccountBalanceChangedV1;
import am.banking.system.common.shared.exception.NotFoundException;
import am.banking.system.common.shared.exception.account.InsufficientFundsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 23.08.25
 * Time: 02:06:58
 */
@Service
@RequiredArgsConstructor
public class AccountService implements AccountServiceUseCase {
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final AccountRepository accountRepository;
    private final R2dbcTransactionManager transactionManager;

    @Override
    public Mono<Void> applyDebit(UUID accountId, BigDecimal amount) {
        TransactionalOperator txOperator = TransactionalOperator.create(transactionManager);

        return accountRepository.findByIdForUpdate(accountId)
                .switchIfEmpty(Mono.error(new NotFoundException("Account not found")))
                .flatMap(acc -> {
                    if (acc.getBalance().compareTo(amount) < 0) {
                        return Mono.error(new InsufficientFundsException("Insufficient funds"));
                    }

                    final Account update = acc.withBalance(acc.getBalance().subtract(amount));

                    return accountRepository.save(update)
                            .flatMap(saved -> {
                                final AccountBalanceChangedV1 event = new AccountBalanceChangedV1(
                                        UUID.randomUUID(),
                                        saved.getId(),
                                        saved.getBalance(),
                                        saved.getVersion(),
                                        Instant.now()
                                );

                                final OutboxEvent outboxEvent = OutboxEvent.from(
                                        "Account",
                                        saved.getId(),
                                        "AccountBalanceChangedV1",
                                        event,
                                        objectMapper
                                );

                                return outboxRepository.save(outboxEvent).then();
                            });
                })
                .as(txOperator::transactional);
    }
}