package am.banking.system.account.outbox;

import am.banking.system.common.shared.outbox.GenericOutboxEvent;
import am.banking.system.common.shared.outbox.JsonEventSerializer;
import am.banking.system.common.shared.outbox.OutboxEvent;
import am.banking.system.common.shared.outbox.OutboxStatus;

/**
 * Author: Artyom Aroyan
 * Date: 09.09.25
 * Time: 19:00:47
 */
public class OutboxMapper {
    public static OutboxEventEntity toEntity(OutboxEvent event) {
        OutboxEventEntity entity = new OutboxEventEntity();
        entity.setId(event.getId());
        entity.setAggregateType(event.getAggregateType());
        entity.setAggregateId(event.getAggregateId());
        entity.setType(event.getType());
        entity.setPayload(getSerializedPayload(event));
        entity.setStatus(OutboxStatus.PENDING);
        entity.setCreatedAt(event.getCreatedAt());
        return entity;
    }

    private static String getSerializedPayload(OutboxEvent event) {
        if (event instanceof GenericOutboxEvent generic) {
            return JsonEventSerializer.serialize(generic.getPayload());
        } else {
            return JsonEventSerializer.serialize(event);
        }
    }
}