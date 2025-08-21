package am.banking.system.transaction.application.validation;

import am.banking.system.transaction.api.dto.TransactionRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 21.08.25
 * Time: 23:59:56
 */
@Component
public class RequestValidator implements RequestValidation {
    @Override
    public Mono<Boolean> isValidRequest(TransactionRequest request) {
        return null;
    }

    // ToDo: validate debit account amount, is in that account enough balance which client try to transfer.
    // ToDo: validate credit account existences.
    // ToDo: validate user permissions, this should be in api gateway, if client arrived here than hes has permissions or api gateway is not working as must.
    // ToDo:
    // ToDo:
    // ToDo:
}