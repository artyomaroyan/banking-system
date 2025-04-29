package am.banking.system.security.exception;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 01:02:24
 */
public class NotFoundTokenTypeException extends RuntimeException {
    public NotFoundTokenTypeException(String message) {
        super(message);
    }
}