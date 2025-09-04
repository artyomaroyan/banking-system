package am.banking.system.transaction.application.port.in;

import am.banking.system.transaction.api.dto.TransactionRequest;
import am.banking.system.transaction.domain.model.Transaction;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.08.25
 * Time: 21:01:49
 */
public interface TransactionUseCase {
    Mono<Void> transferToCard(TransactionRequest request);
    Mono<Void> transferToAccount(TransactionRequest request);
    Mono<Transaction> createTransaction(UUID userId, UUID transferId, String from, String to, String amountStr, String idempotencyKey);
}