package am.banking.system.transaction.projection;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 24.08.25
 * Time: 20:08:36
 */
@Repository
@RequiredArgsConstructor
public class BalanceProjectionRepository {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public Mono<Integer> upsertIfNewer(UUID accountId, BigDecimal balance, long version, Instant timestamp) {
        return r2dbcEntityTemplate.select(AccountBalanceProjection.class)
                .matching(Query.query(Criteria.where("accountId").is(accountId)))
                .one()
                .flatMap(existing -> {
                    if (existing.getVersion() < version) {

                        return r2dbcEntityTemplate.update(AccountBalanceProjection.class)
                                .matching(Query.query(Criteria.where("accountId").is(accountId)))
                                .apply(Update.update("balance", balance)
                                        .set("version", version)
                                        .set("updatedAt", timestamp))
                                .map(Number::intValue);
                    } else {
                        return Mono.just(0);
                    }
                })
                .switchIfEmpty(r2dbcEntityTemplate.insert(AccountBalanceProjection.class)
                        .using(new AccountBalanceProjection(accountId, balance, version, timestamp))
                        .thenReturn(1)
                );
    }
}