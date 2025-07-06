package am.banking.system.common.shared.exception.security.keystore;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 00:47:29
 */
public class KeyStoreLoadException extends RuntimeException {
    public KeyStoreLoadException(String message) {
        super(message);
    }

    public KeyStoreLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}