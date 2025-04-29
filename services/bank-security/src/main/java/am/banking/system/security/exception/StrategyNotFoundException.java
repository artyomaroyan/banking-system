package am.banking.system.security.exception;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:36:37
 */
public class StrategyNotFoundException extends RuntimeException {
    public StrategyNotFoundException(String message) {
        super(message);
    }
}