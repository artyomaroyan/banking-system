package am.banking.system.user.application.service.account;

import am.banking.system.user.application.port.in.account.BankAccountUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Author: Artyom Aroyan
 * Date: 09.08.25
 * Time: 00:05:27
 */
@Service
public class BankAccountService implements BankAccountUseCase {

    @Override
    public Mono<String> getAccountNumber() {
        return null;
    }

    @Override
    public Mono<BigDecimal> getAccountBalance() {
        return null;
    }
}