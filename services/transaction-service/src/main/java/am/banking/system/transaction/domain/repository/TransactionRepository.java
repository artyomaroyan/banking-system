package am.banking.system.transaction.domain.repository;

import am.banking.system.transaction.domain.model.Transaction;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 20.08.25
 * Time: 22:43:23
 */
@Repository
public interface TransactionRepository extends R2dbcRepository<Transaction, UUID> {
}