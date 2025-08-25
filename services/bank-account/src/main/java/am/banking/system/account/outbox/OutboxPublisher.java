package am.banking.system.account.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 23.08.25
 * Time: 01:49:38
 */
@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxRepository outboxRepository;
    private final KafkaSender<String, String> kafkaSender;

    @Scheduled(fixedDelayString = "PT2S") // every 2 seconds; tune as needed
    public void publishBatch() {
        outboxRepository.findTop100ByPublishedFalseOrderByOccurredAtAsc()
                .flatMap(event -> {
                    String topic;
                    if ("AccountBalanceChangedV1".equals(event.getType())) {
                        topic = "account.balance-changed.v1";
                    } else {
                        return Mono.error(new IllegalStateException("Unknown event type: " + event.getType()));
                    }

                    String key = event.getAggregateId().toString();

                    SenderRecord<String, String, UUID> record =
                            SenderRecord.create(topic, null, null, key, event.getPayload(), event.getEventId());

                    return kafkaSender.send(Mono.just(record))
                            .next()
                            .flatMap(_ -> {
                                event.setPublished(true);
                                return outboxRepository.save(event);
                            });
                })
                .subscribe();
    }
}