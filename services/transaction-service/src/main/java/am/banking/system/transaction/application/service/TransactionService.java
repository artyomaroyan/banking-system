package am.banking.system.transaction.application.service;

import am.banking.system.common.messages.ValidateAndReserveCommand;
import am.banking.system.common.shared.dto.transaction.TransactionRequest;
import am.banking.system.common.shared.outbox.GenericOutboxEvent;
import am.banking.system.transaction.application.port.in.TransactionUseCase;
import am.banking.system.transaction.domain.enums.Status;
import am.banking.system.transaction.domain.model.Transaction;
import am.banking.system.transaction.domain.repository.TransactionRepository;
import am.banking.system.transaction.outbox.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
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
    private final OutboxService outboxService;
    private final TransactionRepository transactionRepository;

    @Override
    public Mono<Void> transferToCard(TransactionRequest request) {
        return null;
    }

    @Override
    public Mono<Void> transferToAccount(TransactionRequest request) {
        return null;
    }

    @Override
    public Mono<Transaction> createTransaction(UUID userId, UUID transferId, String from, String to, String amountStr, String idempotencyKey) {
        BigDecimal amount = new BigDecimal(amountStr);
        Mono<Transaction> existing = Mono.empty();
        if (idempotencyKey != null) {
            existing = transactionRepository.findByIdempotencyKey(idempotencyKey);
        }

        return existing.switchIfEmpty(Mono.defer(() -> {
            var entity = Transaction.builder()
                    .userId(userId)
                    .debitAccount(from)
                    .creditAccount(to)
                    .amount(amount)
                    .status(Status.NEW)
                    .idempotencyKey(idempotencyKey)
                    .build();

            return transactionRepository.save(entity)
                    .flatMap(saved -> {
                        var command = new ValidateAndReserveCommand(transferId, from, amount, Instant.now());

                        var outboxEvent = new GenericOutboxEvent(
                                "Transaction",
                                saved.getId().toString(),
                                "ValidateAndReserveCommand",
                                command
                        );

                        return outboxService.saveEvent(outboxEvent)
                                .thenReturn(saved);
                    });
        }));
    }
}