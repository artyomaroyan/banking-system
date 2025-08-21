package am.banking.system.transaction.application.service;

import am.banking.system.transaction.api.dto.TransactionRequest;
import am.banking.system.transaction.application.port.in.TransactionUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 21.08.25
 * Time: 23:56:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService implements TransactionUseCase {

    @Override
    public Mono<Void> transferToCard(TransactionRequest request) {
        return null;
    }

    @Override
    public Mono<Void> transferToAccount(TransactionRequest request) {
        return null;
    }
}