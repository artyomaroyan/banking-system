package am.banking.system.user.application.port.in.password;

import am.banking.system.user.api.dto.PasswordResetConfirmRequest;
import am.banking.system.user.api.dto.PasswordResetEmailRequest;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 25.07.25
 * Time: 21:38:55
 */
public interface PasswordRecoveryHelperUserCase {
    Mono<Void> sendPasswordResetEmail(PasswordResetEmailRequest request);
    Mono<Void> completePasswordReset(PasswordResetConfirmRequest request);
}