package am.banking.system.account.application.service;

import am.banking.system.account.api.dto.AccountResponse;
import am.banking.system.common.shared.dto.user.UserRegistrationEvent;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 15:52:35
 */
public interface IAccountCreationService {
    Mono<AccountResponse> createDefaultAccount(UserRegistrationEvent event);
}