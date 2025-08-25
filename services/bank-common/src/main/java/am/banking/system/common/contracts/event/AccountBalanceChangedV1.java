package am.banking.system.common.contracts.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 23.08.25
 * Time: 01:20:23
 */
public record AccountBalanceChangedV1(
        UUID eventId,
        UUID accountId,
        BigDecimal newBalance,
        long version,
        Instant occurredAt
) {
}