package am.banking.system.account.domain.repository;

import am.banking.system.account.domain.entity.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 15:28:12
 */
@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, String> {
    Mono<Boolean> existsAccountsByAccountNumber(String accountNumber);
}