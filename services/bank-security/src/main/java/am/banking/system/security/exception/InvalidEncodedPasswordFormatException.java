package am.banking.system.security.exception;

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