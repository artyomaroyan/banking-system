package am.banking.system.transaction.application.service;

import am.banking.system.common.events.ValidateAndReserveCommand;
import am.banking.system.common.shared.dto.transaction.TransactionRequest;
import am.banking.system.common.outbox.OutboxStatus;
import am.banking.system.transaction.application.port.in.TransactionUseCase;
import am.banking.system.transaction.domain.enums.TransactionStatus;
import am.banking.system.transaction.domain.enums.TransactionType;
import am.banking.system.transaction.domain.model.Transaction;
import am.banking.system.transaction.domain.repository.TransactionRepository;
import am.banking.system.transaction.outbox.OutboxEventEntity;
import am.banking.system.transaction.outbox.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.08.25
 * Time: 23:56:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService implements TransactionUseCase {
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final TransactionalOperator transactionalOperator;
    private final TransactionRepository transactionRepository;

    @Override
    public Mono<Void> transferToCard(TransactionRequest request) {
        var txId = request.transactionId() != null ? request.transactionId() : UUID.randomUUID();
        return createTransaction(request.userId(),txId, request.from(),request.to(),request.amount(), request.idempotencyKey()).then();
    }

    @Override
    public Mono<Void> transferToAccount(TransactionRequest request) {
        var txId = request.transactionId() != null ? request.transactionId() : UUID.randomUUID();
        return createTransaction(request.userId(), txId, request.from(), request.to(), request.amount(), request.idempotencyKey()).then();
    }

    @Override
    public Mono<Transaction> createTransaction(UUID userId, UUID transactionId, String from, String to, BigDecimal amount, String idempotencyKey) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new IllegalArgumentException("Amount must be greater than zero"));
        }

        Mono<Transaction> existing = idempotencyKey == null ? Mono.empty() : transactionRepository.findByIdempotencyKey(idempotencyKey);

        return Mono.fromCallable(() -> objectMapper.writeValueAsString(new ValidateAndReserveCommand(
                transactionId,
                from,
                amount,
                Instant.now()
        )))
                .flatMap(payload -> existing.switchIfEmpty(Mono.defer(() -> {
                    Transaction toEntity = Transaction.builder()
                            .userId(userId)
                            .debitAccount(from)
                            .creditAccount(to)
                            .amount(amount)
                            .status(TransactionStatus.NEW)
                            .idempotencyKey(idempotencyKey)
                            .type(TransactionType.UNKNOWN)
                            .build();
                    toEntity.setId(transactionId);
                    toEntity.setCreatedAt(LocalDateTime.from(Instant.now()));

                    OutboxEventEntity outboxEntity = OutboxEventEntity.builder()
                            .id(UUID.randomUUID())
                            .topic("accounts.commands")
                            .key(transactionId.toString())
                            .aggregateType("transaction")
                            .aggregateId(transactionId.toString())
                            .type("ValidateAndReserveCommand")
                            .payload(payload)
                            .status(OutboxStatus.PENDING)
                            .tries(0)
                            .createdAt(Instant.now())
                            .build();

                    return transactionalOperator.execute(_ -> transactionRepository.save(toEntity)
                            .flatMap(savedTx -> outboxRepository.save(outboxEntity)
                                    .thenReturn(savedTx)))
                            .single();
                })));
    }
}