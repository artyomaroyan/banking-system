package am.banking.system.user.application.service.auth;

import am.banking.system.common.shared.response.Result;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 21.06.25
 * Time: 18:40:03
 */
public interface IUserAccountActivationService {
    Mono<Result<String>> activateAccount(String activationToken, String username);
}