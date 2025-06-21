package am.banking.system.common.shared.exception.security;

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