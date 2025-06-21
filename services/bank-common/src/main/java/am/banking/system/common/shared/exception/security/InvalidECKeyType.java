package am.banking.system.common.shared.exception.security;

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