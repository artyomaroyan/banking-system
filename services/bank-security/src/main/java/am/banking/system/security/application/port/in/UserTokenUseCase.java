package am.banking.system.security.application.port.in;

import am.banking.system.security.api.shared.UserPrincipal;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:10:49
 */
public interface UserTokenUseCase {
    Mono<String> generateSystemToken();
    Mono<String> generateJwtAccessToken(final UserPrincipal principal);
    Mono<String> generatePasswordResetToken(final UserPrincipal principal);
    Mono<String> generateEmailVerificationToken(final UserPrincipal principal);
}