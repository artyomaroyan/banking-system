package am.banking.system.common.shared.exception.security.password;

/**
 * Author: Artyom Aroyan
 * Date: 05.07.25
 * Time: 23:08:50
 */
public class PasswordValidationException extends RuntimeException {
    public PasswordValidationException(String message) {
        super(message);
    }
}