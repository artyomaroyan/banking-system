package am.banking.system.user.application.port.out.account;

import am.banking.system.common.shared.dto.account.AccountRequest;
import am.banking.system.common.shared.dto.account.AccountResponse;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 16.07.25
 * Time: 23:45:24
 */
public interface CurrentAccountCreationClientPort {
    Mono<AccountResponse> createDefaultAccount(@Valid AccountRequest request);
}