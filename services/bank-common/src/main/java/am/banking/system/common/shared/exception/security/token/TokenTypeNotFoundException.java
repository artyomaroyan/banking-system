package am.banking.system.common.shared.exception.security.token;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 01:02:24
 */
public class TokenTypeNotFoundException extends RuntimeException {
    public TokenTypeNotFoundException(String message) {
        super(message);
    }
}