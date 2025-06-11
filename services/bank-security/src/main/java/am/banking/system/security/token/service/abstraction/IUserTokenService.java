package am.banking.system.security.token.service.abstraction;

import am.banking.system.security.model.dto.UserPrincipal;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:10:49
 */
public interface IUserTokenService {
    Mono<String> generatePasswordResetToken(final UserPrincipal principal);
    Mono<String> generateEmailVerificationToken(final UserPrincipal principal);
    Mono<Long> markTokensForciblyExpired();
}