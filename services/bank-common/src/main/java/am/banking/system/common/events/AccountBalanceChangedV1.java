package am.banking.system.common.events;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 23.08.25
 * Time: 01:20:23
 */
@Validated
public record AccountBalanceChangedV1(
        @NotNull UUID eventId,
        @NotNull UUID accountId,
        @NotNull @Positive BigDecimal newBalance,
        @NotNull long version,
        @NotNull Instant occurredAt) implements DomainEvent {

    @Override
    public UUID eventId() {
        return eventId;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}