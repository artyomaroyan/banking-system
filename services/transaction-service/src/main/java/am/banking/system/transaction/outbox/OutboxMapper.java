package am.banking.system.transaction.outbox;

import am.banking.system.common.outbox.GenericOutboxEvent;
import am.banking.system.common.outbox.JsonEventSerializer;
import am.banking.system.common.outbox.OutboxEvent;
import am.banking.system.common.outbox.OutboxStatus;

/**
 * Author: Artyom Aroyan
 * Date: 09.09.25
 * Time: 18:33:42
 */
public final class OutboxMapper {

    private OutboxMapper() {
    }

    public static OutboxEventEntity toEntity(OutboxEvent event, String topic, String key) {
        String payload;
        if (event instanceof GenericOutboxEvent generic) {
            payload = JsonEventSerializer.serialize(generic.getPayload());
        } else {
            payload = JsonEventSerializer.serialize(event);
        }

        return OutboxEventEntity.builder()
                .id(event.getId())
                .topic(topic)
                .key(key)
                .aggregateType(event.getAggregateType())
                .aggregateId(event.getAggregateId())
                .type(event.getType())
                .payload(payload)
                .status(OutboxStatus.PENDING)
                .tries(0)
                .createdAt(event.getCreatedAt())
                .build();
    }
}