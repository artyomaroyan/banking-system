package am.banking.system.transaction.outbox;

import am.banking.system.common.outbox.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 09.09.25
 * Time: 18:35:44
 */
@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;

    public Mono<Void> saveEvent(OutboxEvent event, String topic, String key) {
        OutboxEventEntity eventEntity = OutboxMapper.toEntity(event, topic, key);
        return outboxRepository.save(eventEntity).then();
    }

    public Mono<Void> saveEntity(OutboxEventEntity entity) {
        return outboxRepository.save(entity).then();
    }
}