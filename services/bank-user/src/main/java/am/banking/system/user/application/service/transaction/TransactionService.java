package am.banking.system.user.application.service.transaction;

import am.banking.system.common.shared.dto.transaction.TransactionRequest;
import am.banking.system.user.application.port.in.TransactionUseCase;
import am.banking.system.user.application.port.out.transaction.TransactionClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 07.09.25
 * Time: 22:31:57
 */
@Service
@RequiredArgsConstructor
public class TransactionService implements TransactionUseCase {
    private final TransactionClientPort transactionClient;

    @Override
    public Mono<Map<String, String>> makeTransfer(TransactionRequest request, String idempotencyKey) {
        return transactionClient.createTransfer(request, idempotencyKey);
    }
}