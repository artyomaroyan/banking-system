package am.banking.system.user.application.port.in.user;

import am.banking.system.common.shared.response.Result;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.06.25
 * Time: 18:40:03
 */
public interface UserAccountActivationUseCase {
    Mono<Result<String>> activateAccount(UUID userId, String username, String activationToken);
}