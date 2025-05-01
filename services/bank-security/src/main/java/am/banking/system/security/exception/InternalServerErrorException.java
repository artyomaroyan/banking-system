package am.banking.system.security.exception;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 15:05:05
 */
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}