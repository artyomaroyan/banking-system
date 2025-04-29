package am.banking.system.security.token.validator.abstraction;

import am.banking.system.security.model.enums.TokenType;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:20:19
 */
public interface IUserTokenValidator {
    boolean isValidEmailVerificationToken(final String token);
    boolean isValidPasswordResetToken(final String token);
    String extractUsername(final String token, final TokenType type);
}