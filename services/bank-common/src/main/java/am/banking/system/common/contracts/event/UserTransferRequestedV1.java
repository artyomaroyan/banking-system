//package am.banking.system.common.contracts.event;
//
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Positive;
//import org.springframework.validation.annotation.Validated;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.util.UUID;
//
//**
// * Author: Artyom Aroyan
// * Date: 26.08.25
// * Time: 02:27:42
// */
//@Validated
//public record UserTransferRequestedV1(@NotNull UUID eventId, @NotNull UUID userId, @NotNull UUID debitAccountId,
//                                      @NotNull UUID creditAccountId, @NotNull @Positive BigDecimal amount,
//                                      @NotNull Instant occurredAt) implements DomainEvent {
//}