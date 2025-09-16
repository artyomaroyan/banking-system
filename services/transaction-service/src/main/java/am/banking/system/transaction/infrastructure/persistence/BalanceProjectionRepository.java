//package am.banking.system.transaction.infrastructure.persistence;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.r2dbc.core.DatabaseClient;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Mono;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.util.UUID;
//
//**
// * Author: Artyom Aroyan
// * Date: 11.09.25
// * Time: 17:07:18
// */
//@Repository
//@RequiredArgsConstructor
//public class BalanceProjectionRepository {
//    private final DatabaseClient databaseClient;
//
//    public Mono<Integer> upsertIfNewer(UUID accountId, BigDecimal balance, long version, Instant timestamp) {
//        String sql = """
//                INSERT INTO transaction.account_balance_projection (account_id, balance, version, updated_at)
//                VALUES (:accountId, :balance, :version, :updatedAt)
//                ON CONFLICT (account_id) DO UPDATE
//                SET balance = EXCLUDED.balance,
//                version = EXCLUDED.version,
//                updated_at = EXCLUDED.updated_at
//                WHERE transaction.account_balance_projection.version < EXCLUDED.version
//                """;
//
//        return databaseClient.sql(sql)
//                .bind("accountId", accountId)
//                .bind("balance", balance)
//                .bind("version", version)
//                .bind("updatedAt", timestamp)
//                .fetch()
//                .rowsUpdated()
//                .map(Long::intValue);
//    }
//}