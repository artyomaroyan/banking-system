package am.banking.system.security.exception;

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