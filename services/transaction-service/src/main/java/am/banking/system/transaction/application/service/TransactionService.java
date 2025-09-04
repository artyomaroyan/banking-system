package am.banking.system.transaction.application.service;

import am.banking.system.common.messages.ValidateAndReserveCommand;
import am.banking.system.transaction.api.dto.TransactionRequest;
import am.banking.system.transaction.application.port.in.TransactionUseCase;
import am.banking.system.transaction.domain.enums.Status;
import am.banking.system.transaction.domain.model.Transaction;
import am.banking.system.transaction.domain.repository.TransactionRepository;
import am.banking.system.transaction.outbox.OutboxEvent;
import am.banking.system.transaction.outbox.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public Mono<Void> transferToCard(TransactionRequest request) {
        return null;
    }

    @Override
    public Mono<Void> transferToAccount(TransactionRequest request) {
        return null;
    }

    private Mono<Transaction> createTransaction(UUID userId, UUID transferId, String from, String to, String amountStr, String idempotencyKey) {
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

                        try {
                            var payload = objectMapper.writeValueAsString(command);
                            var outbox = new OutboxEvent();
                            outbox.setId(UUID.randomUUID());
                            outbox.setTopic("accounts.commands");
                            outbox.setKey(from);
                            outbox.setPayload(payload);
                            outbox.setHeader("{\"correlationId\":\"" + transferId + "\"}");
                            outbox.setPublished(false);
                            outbox.setCreatedAt(Instant.now());
                            return outboxRepository.save(outbox).thenReturn(saved);
                        } catch (Exception e) {
                            return Mono.error(e);
                        }
                    });
        }));
    }
}