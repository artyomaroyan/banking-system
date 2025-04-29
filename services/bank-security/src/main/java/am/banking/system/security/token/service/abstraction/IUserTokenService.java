package am.banking.system.security.token.service.abstraction;

import am.banking.system.security.model.dto.UserPrincipal;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:10:49
 */
public interface IUserTokenService {
    String generatePasswordResetToken(final UserPrincipal principal);
    String generateEmailVerificationToken(final UserPrincipal principal);
    void markTokenAsVerified(final Long token);
}