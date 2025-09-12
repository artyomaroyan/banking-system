package am.banking.system.transaction.outbox;

import am.banking.system.common.outbox.OutboxStatus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.time.Duration;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 04.09.25
 * Time: 22:48:59
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private Disposable disposable;
    private final OutboxRepository outboxRepository;
    private final KafkaSender<String, String> kafkaSender;

    @PostConstruct
    public void start() {
        disposable = Flux.interval(Duration.ofSeconds(1))
                .flatMap(i -> outboxRepository.findByStatus(OutboxStatus.PENDING))
                .flatMap(this::sendAndMark)
                .onErrorContinue((ex, obj) -> log.error("Outbox publisher error", ex))
                .subscribe();
    }

    @PreDestroy
    public void stop() {
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }

    private Mono<Void> sendAndMark(OutboxEventEntity entity) {
        ProducerRecord<String, String> record = new ProducerRecord<>(entity.getTopic(), entity.getKey(), entity.getPayload());
        SenderRecord<String, String, UUID> senderRecord = SenderRecord.create(record, entity.getId());

        return kafkaSender.send(Mono.just(senderRecord))
                .next()
                .flatMap(_ -> {
                    entity.setStatus(OutboxStatus.SENT);
                    return outboxRepository.save(entity).then();
                })
                .onErrorResume(error -> {
                    log.error("Failed to send outbox event {}: {}", entity.getId(), error.toString());
                    var tries = entity.getTries() == null ? 1 : entity.getTries() + 1;
                    entity.setTries(tries);
                    entity.setLastError(error.getMessage());
                    if (tries >= 5) {
                        entity.setStatus(OutboxStatus.FAILED);
                    }
                    return outboxRepository.save(entity).then();
                });
    }
}