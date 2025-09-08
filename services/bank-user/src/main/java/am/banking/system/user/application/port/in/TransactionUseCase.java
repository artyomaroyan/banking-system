package am.banking.system.user.application.port.in;

import am.banking.system.common.shared.dto.transaction.TransactionRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 07.09.25
 * Time: 22:30:37
 */
public interface TransactionUseCase {
    Mono<Map<String, String>> makeTransfer(TransactionRequest request, String idempotencyKey);
}