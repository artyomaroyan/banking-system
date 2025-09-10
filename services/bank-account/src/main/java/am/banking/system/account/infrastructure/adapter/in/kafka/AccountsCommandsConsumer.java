package am.banking.system.account.infrastructure.adapter.in.kafka;

import am.banking.system.account.domain.repository.AccountRepository;
import am.banking.system.account.outbox.OutboxEventEntity;
import am.banking.system.account.outbox.OutboxRepository;
import am.banking.system.common.messages.FundsInsufficientEvent;
import am.banking.system.common.messages.FundsReservedEvent;
import am.banking.system.common.messages.ValidateAndReserveCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;

import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 08.09.25
 * Time: 23:01:43
 */
@Component
@RequiredArgsConstructor
public class AccountsCommandsConsumer {
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final AccountRepository accountRepository;
    private final KafkaReceiver<String, String> kafkaReceiver;

    public void start() {
        kafkaReceiver.receive()
                .flatMap(record -> {
                    try {
                        ValidateAndReserveCommand command = objectMapper.readValue(record.value(), ValidateAndReserveCommand.class);
                        return handleValidateAndReserve(command)
                                .then(Mono.fromRunnable(record.receiverOffset()::acknowledge));
                    } catch (Exception ex) {
                        return Mono.error(ex);
                    }
                })
                .subscribe();
    }

    private Mono<Void> handleValidateAndReserve(ValidateAndReserveCommand command) {
        return accountRepository.findAccountByAccountNumber(command.fromAccount())
                .flatMap(acc -> {
                    var available = acc.getBalance().subtract(acc.getReserved());
                    if (available.compareTo(command.amount()) >= 0) {
                        acc.toBuilder().reserved(acc.getReserved().add(command.amount())).build();

                        return accountRepository.save(acc)
                                .flatMap(_ -> {
                                    var event = new FundsReservedEvent(
                                            command.transferId(),
                                            UUID.randomUUID().toString(),
                                            command.fromAccount(),
                                            command.amount(),
                                            Instant.now());

                                    try {
                                        var outbox = new OutboxEventEntity();
                                        outbox.setId(UUID.randomUUID());
                                        outbox.setAggregateType("accounts.events");
                                        outbox.setPayload(objectMapper.writeValueAsString(event));
                                        outbox.setCreatedAt(Instant.now());
                                        return outboxRepository.save(outbox).then();
                                    } catch (Exception ex) {
                                        return Mono.error(ex);
                                    }
                                });
                    } else {
                        var event = new FundsInsufficientEvent(
                                command.transferId(),
                                command.fromAccount(),
                                command.amount(),
                                "INSUFFICIENT_FUNDS",
                                Instant.now()
                        );

                        try {
                            var outbox = new OutboxEventEntity();
                            outbox.setId(UUID.randomUUID());
                            outbox.setAggregateType("accounts.events");
                            outbox.setPayload(objectMapper.writeValueAsString(event));
                            outbox.setCreatedAt(Instant.now());
                            return outboxRepository.save(outbox).then();
                        } catch (Exception ex) {
                            return Mono.error(ex);
                        }
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    try {
                        var event = new FundsInsufficientEvent(
                                command.transferId(),
                                command.fromAccount(),
                                command.amount(),
                                "ACCOUNT_NOT_FOUND",
                                Instant.now()
                        );
                        var outbox = new OutboxEventEntity();
                        outbox.setId(UUID.randomUUID());
                        outbox.setAggregateType("accounts.events");
                        outbox.setPayload(objectMapper.writeValueAsString(event));
                        outbox.setCreatedAt(Instant.now());
                        return outboxRepository.save(outbox).then();
                    } catch (Exception ex) {
                        return Mono.error(ex);
                    }
                }));
    }
}