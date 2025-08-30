package am.banking.system.account.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.Map;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 23.08.25
 * Time: 01:49:38
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxRepository outboxRepository;
    private final KafkaSender<String, String> kafkaSender;

    private final Map<String, String> topicMapping = Map.of(
            "AccountBalanceChangedV1", "account.balance-changed.v1",
            "UserTransferRequestedV1", "user.transfer-requested.v1"
    );

    @Scheduled(fixedDelayString = "${outbox.publish-interval}") // every 2 seconds; tune as needed
    public void publishBatch() {
        outboxRepository.findTop100ByPublishedFalseOrderByOccurredAtAsc()
                .collectList()
                .filter(list -> !list.isEmpty())
                .flatMapMany(Flux::fromIterable)
                .flatMap(this::sendEventToKafka)
                .subscribe(
                        success -> log.info("Published event {}", success),
                        error -> log.error("Error publishing outbox events", error)
                );
    }

    private Mono<UUID> sendEventToKafka(OutboxEvent event) {
        String topic = topicMapping.get(event.getType());
        if (topic == null) {
            log.warn("Unknown event type: {}", event.getType());
            return Mono.empty();
        }

        SenderRecord<String, String, UUID> rec = SenderRecord.create(topic, null, null,
                event.getAggregateId().toString(), event.getPayload(), event.getEventId());

        return kafkaSender.send(Mono.just(rec))
                .doOnError(err -> log.error("Kafka send failed for event {}", event.getEventId(), err))
                .then(Mono.just(event.getEventId()));
    }
}