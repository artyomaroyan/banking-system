package am.banking.system.account.application.port.in;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 23.08.25
 * Time: 02:07:13
 */
public interface AccountServiceUseCase {
    Mono<Void> applyDebit(UUID accountId, BigDecimal amount);
}