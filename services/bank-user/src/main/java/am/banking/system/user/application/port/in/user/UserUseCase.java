package am.banking.system.user.application.port.in.user;

import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.domain.model.User;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 21.06.25
 * Time: 18:42:59
 */
public interface UserUseCase {
    Mono<User> createUser(UserRequest request);
    Mono<User> updateUser(User existingUser, UserRequest request);
}