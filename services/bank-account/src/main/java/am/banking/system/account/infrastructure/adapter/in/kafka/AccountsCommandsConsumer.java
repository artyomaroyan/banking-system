package am.banking.system.account.infrastructure.adapter.in.kafka;

import am.banking.system.account.domain.repository.AccountRepository;
import am.banking.system.account.outbox.OutboxEventEntity;
import am.banking.system.account.outbox.OutboxRepository;
import am.banking.system.common.events.FundsInsufficientEvent;
import am.banking.system.common.events.FundsReservedEvent;
import am.banking.system.common.events.ValidateAndReserveCommand;
import am.banking.system.common.outbox.OutboxStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 08.09.25
 * Time: 23:01:43
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccountsCommandsConsumer {
    private Disposable disposable;
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final AccountRepository accountRepository;
    private final KafkaReceiver<String, String> kafkaReceiver;

    @PostConstruct
    public void start() {
        disposable = kafkaReceiver.receive()
                .flatMap(this::process)
                .doOnError(e -> log.error("AccountsCommandsConsumer error", e))
                .retry()
                .subscribe();
    }

    @PreDestroy
    public void stop() {
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }

    private Mono<Void> process(ReceiverRecord<String, String> record) {
        return Mono.fromCallable(() -> objectMapper.readValue(record.value(), ValidateAndReserveCommand.class))
                .flatMap(this::handleValidateAndReserve)
                .then(Mono.fromRunnable(record.receiverOffset()::acknowledge))
                .onErrorResume(ex -> {
                    log.error("Failed to process command, message: {}", record.value(), ex);
                    record.receiverOffset().acknowledge();
                    return Mono.empty();
                }).then();
    }

    private Mono<Void> handleValidateAndReserve(ValidateAndReserveCommand command) {
        return accountRepository.findAccountByAccountNumber(command.fromAccount())
                .flatMap(acc -> {
                    var available = acc.getBalance().subtract(acc.getReserved());
                    if (available.compareTo(command.amount()) >= 0) {
                        var updated = acc.toBuilder()
                                .reserved(acc.getReserved().add(command.amount()))
                                .build();

                        return accountRepository.save(updated)
                                .then(Mono.fromCallable(() -> {
                                    var event = new FundsReservedEvent(
                                            command.transferId(),
                                            UUID.randomUUID().toString(),
                                            command.fromAccount(),
                                            command.amount(),
                                            Instant.now());
                                    return objectMapper.writeValueAsString(event);

                                }).flatMap(payload -> outboxRepository.save(new OutboxEventEntity(
                                        UUID.randomUUID(),
                                        "accounts.events",
                                        command.fromAccount(),
                                        payload,
                                        "{\"correlationId\":\"" + command.transferId() + "\"}",
                                        OutboxStatus.PENDING,
                                        Instant.now()))
                                        .then()));
                    } else {
                        return Mono.fromCallable(() -> objectMapper.writeValueAsString(
                                new FundsInsufficientEvent(
                                        command.transferId(),
                                        command.fromAccount(),
                                        command.amount(),
                                        "INSUFFICIENT_FUNDS",
                                        Instant.now())))

                                .flatMap(payload -> outboxRepository.save(new OutboxEventEntity(
                                        UUID.randomUUID(),
                                        "accounts.events",
                                        command.fromAccount(),
                                        payload,
                                        "{\"correlationId\":\"" + command.transferId() + "\"}",
                                        OutboxStatus.FAILED,
                                        Instant.now()))
                                        .then());
                    }
                })
                .switchIfEmpty(Mono.defer(() ->
                        Mono.fromCallable(() ->
                                objectMapper.writeValueAsString(
                                        new FundsInsufficientEvent(
                                                command.transferId(),
                                                command.fromAccount(),
                                                command.amount(),
                                                "ACCOUNT_NOT_FOUND",
                                                Instant.now())))
                                .flatMap(payload -> outboxRepository.save(new OutboxEventEntity(
                                        UUID.randomUUID(),
                                        "accounts.events",
                                        command.fromAccount(),
                                        payload,
                                        "{\"correlationId\":\"" + command.transferId() + "\"}",
                                        OutboxStatus.FAILED,
                                        Instant.now()
                                ))
                                        .then())));
    }
}