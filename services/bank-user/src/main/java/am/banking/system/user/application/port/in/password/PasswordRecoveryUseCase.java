package am.banking.system.user.application.port.in.password;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.PasswordResetConfirmRequest;
import am.banking.system.user.api.dto.PasswordResetEmailRequest;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 24.07.25
 * Time: 22:33:35
 */
public interface PasswordRecoveryUseCase {
    Mono<Result<String>> resetPassword(PasswordResetConfirmRequest request);
    Mono<Result<String>> sendPasswordResetEmail(PasswordResetEmailRequest request);
}