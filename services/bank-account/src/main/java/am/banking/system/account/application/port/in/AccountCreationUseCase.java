package am.banking.system.account.application.port.in;

import am.banking.system.common.shared.dto.account.AccountResponse;
import am.banking.system.common.shared.dto.user.UserRegistrationEvent;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 16.07.25
 * Time: 22:47:41
 */
public interface AccountCreationUseCase {
    Mono<AccountResponse> createDefaultAccount(UserRegistrationEvent event);
}