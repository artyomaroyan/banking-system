package am.banking.system.transaction.outbox;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 27.08.25
 * Time: 01:45:03
 */
@Repository
public interface OutboxRepository extends R2dbcRepository<OutboxEvent, UUID> {
}