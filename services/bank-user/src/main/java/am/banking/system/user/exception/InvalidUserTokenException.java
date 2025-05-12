package am.banking.system.user.exception;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 22:51:50
 */
public class InvalidUserTokenException extends RuntimeException {
    public InvalidUserTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserTokenException(String message) {
        super(message);
    }
}