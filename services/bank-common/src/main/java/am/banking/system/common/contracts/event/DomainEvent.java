package am.banking.system.common.contracts.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 26.08.25
 * Time: 02:26:39
 */
public interface DomainEvent {
    UUID eventId();
    Instant occurredAt();
}