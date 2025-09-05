package am.banking.system.transaction.application.validation;

import am.banking.system.common.shared.dto.transaction.TransactionRequest;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 21.08.25
 * Time: 23:58:39
 */
public interface RequestValidation {
    Mono<Boolean> isValidRequest(TransactionRequest request);
}