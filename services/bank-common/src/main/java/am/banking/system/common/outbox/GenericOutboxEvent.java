package am.banking.system.common.outbox;

import lombok.Getter;

/**
 * Author: Artyom Aroyan
 * Date: 09.09.25
 * Time: 18:49:14
 */
public class GenericOutboxEvent extends OutboxEvent {
    @Getter
    private final Object payload;

    public GenericOutboxEvent(String aggregateType, String aggregateId, String type, Object payload) {
        super(aggregateType, aggregateId, type);
        this.payload = payload;
    }
}