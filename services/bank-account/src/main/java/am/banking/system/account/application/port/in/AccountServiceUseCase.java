//package am.banking.system.account.application.port.in;
//
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Positive;
//import reactor.core.publisher.Mono;
//
//import java.math.BigDecimal;
//import java.util.UUID;
//
///**
// * Author: Artyom Aroyan
// * Date: 23.08.25
// * Time: 02:07:13
// */
//public interface AccountServiceUseCase {
//    Mono<Void> applyDebit(@NotNull UUID debitAccountId, @NotNull @Positive BigDecimal amount);
//
//    Mono<Void> applyCredit(@NotNull UUID creditAccountId, @NotNull @Positive BigDecimal amount);
//}