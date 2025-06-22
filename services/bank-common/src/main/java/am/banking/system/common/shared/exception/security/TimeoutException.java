package am.banking.system.common.shared.exception.security;

/**
 * Author: Artyom Aroyan
 * Date: 22.06.25
 * Time: 22:32:15
 */
public class TimeoutException extends RuntimeException {
    public TimeoutException(String message) {
        super(message);
    }
}