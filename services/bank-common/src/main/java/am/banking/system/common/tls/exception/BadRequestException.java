package am.banking.system.common.tls.exception;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 15:00:36
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}