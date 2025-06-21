package am.banking.system.common.shared.exception.security;

/**
 * Author: Artyom Aroyan
 * Date: 25.04.25
 * Time: 01:18:54
 */
public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}