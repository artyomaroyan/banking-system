package am.banking.system.common.shared.exception.user;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 22:32:49
 */
public class UserAccountActivationException extends RuntimeException {
    public UserAccountActivationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAccountActivationException(String message) {
        super(message);
    }
}