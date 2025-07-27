package am.banking.system.user.application.port.in.password;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.user.api.dto.PasswordResetRequest;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 25.07.25
 * Time: 21:38:55
 */
public interface PasswordRecoveryFactoryUserCase {
    Mono<TokenResponse> resetPassword(PasswordResetRequest request);
    Mono<Void> sendPasswordResetEmail(PasswordResetRequest request);
}