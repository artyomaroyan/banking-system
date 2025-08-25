package am.banking.system.common.contracts.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public record AccountBalanceChangedV1(@NotNull UUID eventId, @NotNull UUID accountId, @NotNull @Positive BigDecimal newBalance,
                                      @NotNull long version, @NotNull Instant occurredAt) implements DomainEvent {

    @JsonCreator
    public AccountBalanceChangedV1(@JsonProperty("eventId") UUID eventId, @JsonProperty("accountId") UUID accountId,
                                   @JsonProperty("newBalance") BigDecimal newBalance, @JsonProperty("version") long version,
                                   @JsonProperty("occurredAt") Instant occurredAt) {
        this.eventId = eventId;
        this.accountId = accountId;
        this.newBalance = newBalance;
        this.version = version;
        this.occurredAt = occurredAt;
    }
}