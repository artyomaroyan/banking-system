//package am.banking.system.account.application.service;
//
//import am.banking.system.account.application.port.in.AccountServiceUseCase;
//import am.banking.system.account.domain.entity.Account;
//import am.banking.system.account.domain.repository.AccountRepository;
//import am.banking.system.account.outbox.OutboxEventEntity;
//import am.banking.system.account.outbox.OutboxRepository;
//import am.banking.system.common.events.AccountBalanceChangedV1;
//import am.banking.system.common.shared.exception.NotFoundException;
//import am.banking.system.common.shared.exception.account.InsufficientFundsException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.r2dbc.connection.R2dbcTransactionManager;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.reactive.TransactionalOperator;
//import reactor.core.publisher.Mono;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.util.UUID;
//
//**
// * Author: Artyom Aroyan
// * Date: 23.08.25
// * Time: 02:06:58
// */
//@Service
//@RequiredArgsConstructor
//public class AccountService implements AccountServiceUseCase {
//    private final ObjectMapper objectMapper;
//    private final OutboxRepository outboxRepository;
//    private final AccountRepository accountRepository;
//    private final R2dbcTransactionManager transactionManager;
//
//    @Override
//    public Mono<Void> applyDebit(UUID debitAccountId, BigDecimal amount) {
//        return applyBalance(debitAccountId, amount);
//    }
//
//    @Override
//    public Mono<Void> applyCredit(UUID creditAccountId, BigDecimal amount) {
//        return applyBalance(creditAccountId, amount);
//    }
//
//    private Mono<Void> applyBalance(UUID accountId, BigDecimal amount) {
//        TransactionalOperator txOperator = TransactionalOperator.create(transactionManager);
//
//        return accountRepository.findByIdForUpdate(accountId)
//                .switchIfEmpty(Mono.error(new NotFoundException("Account not found")))
//                .flatMap(acc -> {
//                    BigDecimal newBalance = acc.getBalance().add(amount);
//                    if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
//                        return Mono.error(new InsufficientFundsException("Insufficient funds"));
//                    }
//
//                    final Account update = acc.withBalance(newBalance);
//
//                    return accountRepository.save(update)
//                            .flatMap(saved -> OutboxEventEntity.from(
//                                    "Account",
//                                    saved.getId(),
//                                    "AccountBalanceChangedV1",
//                                    new AccountBalanceChangedV1(
//                                            UUID.randomUUID(),
//                                            saved.getId(),
//                                            saved.getBalance(),
//                                            saved.getVersion(),
//                                            Instant.now()),
//                                    objectMapper
//                            ))
//                            .flatMap(outboxRepository::save).then();
//                })
//                .as(txOperator::transactional);
//    }
//}