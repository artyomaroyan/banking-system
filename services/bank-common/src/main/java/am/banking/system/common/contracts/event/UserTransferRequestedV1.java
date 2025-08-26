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
 * Date: 26.08.25
 * Time: 02:27:42
 */
@Validated
public record UserTransferRequestedV1(@NotNull UUID eventId, @NotNull UUID userId, @NotNull UUID debitAccountId,
                                      @NotNull UUID creditAccountId, @NotNull @Positive BigDecimal amount,
                                      @NotNull Instant occurredAt) implements DomainEvent {

    @JsonCreator
    public UserTransferRequestedV1(@JsonProperty("eventId") UUID eventId, @JsonProperty("userId") UUID userId,
                                   @JsonProperty("debitAccountId") UUID debitAccountId,
                                   @JsonProperty("creditAccountId") UUID creditAccountId,
                                   @JsonProperty("amount") BigDecimal amount, @JsonProperty("occurredAt") Instant occurredAt) {
        this.eventId = eventId;
        this.userId = userId;
        this.debitAccountId = debitAccountId;
        this.creditAccountId = creditAccountId;
        this.amount = amount;
        this.occurredAt = occurredAt;
    }
}