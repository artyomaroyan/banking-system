package am.banking.system.common.shared.exception.security;

/**
 * Author: Artyom Aroyan
 * Date: 18.05.25
 * Time: 20:52:23
 */
public class KeyIdGenerationException extends RuntimeException {
    public KeyIdGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}