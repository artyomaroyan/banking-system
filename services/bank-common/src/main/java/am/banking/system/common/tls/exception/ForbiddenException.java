package am.banking.system.common.tls.exception;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 15:02:22
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}