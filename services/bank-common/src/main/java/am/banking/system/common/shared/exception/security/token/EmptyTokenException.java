package am.banking.system.common.shared.exception.security.token;

/**
 * Author: Artyom Aroyan
 * Date: 22.06.25
 * Time: 22:20:06
 */
public class EmptyTokenException extends RuntimeException {
    public EmptyTokenException(String message) {
        super(message);
    }
}