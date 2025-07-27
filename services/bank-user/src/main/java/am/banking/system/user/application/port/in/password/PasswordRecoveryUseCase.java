package am.banking.system.user.application.port.in.password;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.PasswordResetRequest;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 24.07.25
 * Time: 22:33:35
 */
public interface PasswordRecoveryUseCase {
    Mono<Result<String>> resetPassword(PasswordResetRequest request);
    Mono<Result<String>> sendPasswordResetEmail(PasswordResetRequest request);
}