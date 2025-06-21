package am.banking.system.user.application.port.in;

import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.domain.entity.User;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 21.06.25
 * Time: 18:42:59
 */
public interface UserFactoryUseCase {
    Mono<User> createUser(UserRequest request);
}