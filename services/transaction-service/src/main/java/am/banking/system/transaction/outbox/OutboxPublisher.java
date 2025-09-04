package am.banking.system.transaction.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

/**
 * Author: Artyom Aroyan
 * Date: 04.09.25
 * Time: 22:48:59
 */
@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final KafkaSender<String, String> kafkaSender;

    // todo: add @Schedule run every second
    public Flux<SenderResult<Void>> publishPending() {
        return outboxRepository.findByPublishedFalse()
                .flatMap(outbox -> {
                    var rec = new ProducerRecord<String, String>(outbox.getTopic(), outbox.getKey(), outbox.getPayload());
                    SenderRecord<String, String, Void> sr = SenderRecord.create(rec,null);

                    return kafkaSender.send(Mono.just(sr))
                            .next()
                            .flatMap(result -> {
                                outbox.setPublished(true);
                                return outboxRepository.save(outbox).thenReturn(result);
                            });
                });
    }
}