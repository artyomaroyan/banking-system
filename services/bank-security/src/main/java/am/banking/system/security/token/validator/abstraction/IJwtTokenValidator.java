package am.banking.system.security.token.validator.abstraction;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:17:52
 */
public interface IJwtTokenValidator {
    boolean isValidToken(String token, String username);
    String extractUsername(final String token);
}