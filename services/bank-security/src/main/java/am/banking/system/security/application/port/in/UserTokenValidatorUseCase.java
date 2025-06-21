package am.banking.system.security.application.port.in;

import am.banking.system.security.domain.enums.TokenType;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:20:19
 */
public interface UserTokenValidatorUseCase {
    boolean isValidEmailVerificationToken(final String token);
    boolean isValidPasswordResetToken(final String token);
    String extractUsername(final String token, final TokenType type);
}