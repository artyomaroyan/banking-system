package am.banking.system.common.shared.exception;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 15:01:37
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}