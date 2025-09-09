package am.banking.system.common.shared.outbox;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 09.09.25
 * Time: 18:21:03
 */
@Getter
public abstract class OutboxEvent {
    private final UUID id;
    private final String aggregateType;
    private final String aggregateId;
    private final String type;
    private final Instant createdAt;

    public OutboxEvent(String aggregateType, String aggregateId, String type) {
        this.id = UUID.randomUUID();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.type = type;
        this.createdAt = Instant.now();
    }
}