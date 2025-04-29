package am.banking.system.security.exception;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 01:01:09
 */
public class InvalidTokenTypeException extends RuntimeException {
    public InvalidTokenTypeException(String message) {
        super(message);
    }
}