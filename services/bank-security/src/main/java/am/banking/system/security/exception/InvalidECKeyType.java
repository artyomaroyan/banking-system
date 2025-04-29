package am.banking.system.security.exception;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 00:48:30
 */
public class InvalidECKeyType extends RuntimeException {
    public InvalidECKeyType(String message) {
        super(message);
    }
}