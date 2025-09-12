package am.banking.system.transaction.infrastructure.adapter.in.kafka;

import am.banking.system.common.events.FundsReservedEvent;
import am.banking.system.transaction.domain.enums.TransactionStatus;
import am.banking.system.transaction.domain.repository.TransactionRepository;
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
import java.time.LocalDateTime;

/**
 * Author: Artyom Aroyan
 * Date: 08.09.25
 * Time: 22:25:30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccountsEventsConsumer {
    private Disposable disposable;
    private final ObjectMapper objectMapper;
    private final TransactionRepository transactionRepository;
    private final KafkaReceiver<String, String> kafkaReceiver;

    @PostConstruct
    public void start() {
        this.disposable = kafkaReceiver.receive()
                .flatMap(this::process)
                .doOnError(e -> log.error("AccountsEventsConsumer error", e))
                .retry()
                .subscribe();
    }

    @PreDestroy
    public void stop() {
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }

    private Mono<Void> process(ReceiverRecord<String, String> record) {
        return Mono.fromCallable(() -> objectMapper.readValue(record.value(), FundsReservedEvent.class))
                .flatMap(event ->
                        transactionRepository.findById(event.transferId())
                                .flatMap(tx -> {
                                    tx.setReservationId(event.reservationId());
                                    tx.setStatus(TransactionStatus.RESERVED);
                                    tx.setUpdatedAt(LocalDateTime.from(Instant.now()));
                                    return transactionRepository.save(tx);
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    log.warn("Transaction not found for transferId={} (FundsReservedEvent)", event.transferId());
                                    return Mono.empty();
                                })))
                .then(Mono.fromRunnable(record.receiverOffset()::acknowledge))
                .onErrorResume(ex -> {
                    log.error("failed to process funds_reserved event: {}", record.value(), ex);
                    return Mono.empty();
                }).then();
    }
}