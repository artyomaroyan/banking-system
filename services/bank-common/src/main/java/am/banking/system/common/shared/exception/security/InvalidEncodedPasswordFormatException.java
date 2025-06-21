package am.banking.system.common.shared.exception.security;

/**
 * Author: Artyom Aroyan
 * Date: 17.04.25
 * Time: 00:09:02
 */
public class InvalidEncodedPasswordFormatException extends RuntimeException {
    public InvalidEncodedPasswordFormatException(String message) {
        super(message);
    }
}