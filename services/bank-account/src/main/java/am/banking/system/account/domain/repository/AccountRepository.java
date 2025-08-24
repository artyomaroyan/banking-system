package am.banking.system.account.domain.repository;

import am.banking.system.account.domain.entity.Account;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 15:28:12
 */
@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, UUID> {
    Mono<Boolean> existsAccountsByAccountNumber(String accountNumber);

    @Query("SELECT * FROM account.account a WHERE a.id = :accountId FOR UPDATE")
    Mono<Account> findByIdForUpdate(UUID accountId);

}