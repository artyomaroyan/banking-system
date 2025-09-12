package am.banking.system.account.outbox;

import am.banking.system.common.outbox.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 09.09.25
 * Time: 19:10:06
 */
@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;

    public Mono<Void> saveEvent(OutboxEvent event) {
        OutboxEventEntity eventEntity = OutboxMapper.toEntity(event);
        return outboxRepository.save(eventEntity).then();
    }
}