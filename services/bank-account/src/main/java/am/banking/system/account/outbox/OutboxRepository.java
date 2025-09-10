package am.banking.system.account.outbox;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 23.08.25
 * Time: 01:49:26
 */
@Repository
public interface OutboxRepository extends ReactiveCrudRepository<OutboxEventEntity, UUID> {
}