package am.banking.system.common.shared.exception.security;

/**
 * Author: Artyom Aroyan
 * Date: 22.06.25
 * Time: 22:22:29
 */
public class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(String message) {
        super(message);
    }
}