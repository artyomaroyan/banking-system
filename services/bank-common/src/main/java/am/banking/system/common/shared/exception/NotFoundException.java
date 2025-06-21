package am.banking.system.common.shared.exception;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 15:03:04
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}