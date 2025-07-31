package am.banking.system.user.application.port.in.user;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.UserResponse;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 21.06.25
 * Time: 18:43:28
 */
public interface UserManagementUseCase {
    Mono<Void> updateUserAccountState(Integer id);
    Mono<Result<UserResponse>> getUserByUsername(String username);
    Mono<Result<UserResponse>> getUserByEmail(String email);
}