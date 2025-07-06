package am.banking.system.common.shared.exception.security.token;

/**
 * Author: Artyom Aroyan
 * Date: 05.07.25
 * Time: 17:24:32
 */
public class TokenValidationException extends RuntimeException {
    public TokenValidationException(String message) {
        super(message);
    }
}