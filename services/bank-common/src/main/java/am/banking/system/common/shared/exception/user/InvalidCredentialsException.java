package am.banking.system.common.shared.exception.user;

/**
 * Author: Artyom Aroyan
 * Date: 09.08.25
 * Time: 00:48:33
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}