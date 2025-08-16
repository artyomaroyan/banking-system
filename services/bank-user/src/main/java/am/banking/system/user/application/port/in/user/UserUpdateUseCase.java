package am.banking.system.user.application.port.in.user;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.api.dto.UserResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 10.08.25
 * Time: 22:40:31
 */
public interface UserUpdateUseCase {
    Mono<Result<UserResponse>> update(UUID userId, UserRequest request);
}