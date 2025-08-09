package am.banking.system.user.application.port.in.account;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Author: Artyom Aroyan
 * Date: 09.08.25
 * Time: 00:04:51
 */
public interface BankAccountUserCase {
    Mono<String> getAccountNumber();
    Mono<BigDecimal> getAccountBalance();
}