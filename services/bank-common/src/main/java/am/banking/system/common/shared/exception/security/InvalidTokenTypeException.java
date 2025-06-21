package am.banking.system.common.shared.exception.security;

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