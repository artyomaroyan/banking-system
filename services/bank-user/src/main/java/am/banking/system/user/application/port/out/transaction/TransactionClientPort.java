package am.banking.system.user.application.port.out.transaction;

import am.banking.system.common.shared.dto.transaction.TransactionRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 31.08.25
 * Time: 16:47:56
 */
public interface TransactionClientPort {
    Mono<Map<String, String>> createTransfer(@RequestBody TransactionRequest request,
                                            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey);
}