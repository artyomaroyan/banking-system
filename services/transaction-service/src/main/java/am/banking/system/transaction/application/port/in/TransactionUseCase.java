package am.banking.system.transaction.application.port.in;

import am.banking.system.common.shared.dto.transaction.TransactionRequest;
import am.banking.system.transaction.domain.model.Transaction;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.08.25
 * Time: 21:01:49
 */
public interface TransactionUseCase {
    Mono<Void> transferToCard(TransactionRequest request);
    Mono<Void> transferToAccount(TransactionRequest request);
    Mono<Transaction> createTransaction(UUID userId, UUID transactionId, String from, String to, BigDecimal amount, String idempotencyKey);
}