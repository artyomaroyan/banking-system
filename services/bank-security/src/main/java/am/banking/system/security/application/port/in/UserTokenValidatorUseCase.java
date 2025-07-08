package am.banking.system.security.application.port.in;

import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:20:19
 */
public interface UserTokenValidatorUseCase {
    Mono<Boolean> isValidEmailVerificationToken(final String token);
    Mono<Boolean> isValidPasswordResetToken(final String token);
}