package am.banking.system.common.shared.exception.account;

/**
 * Author: Artyom Aroyan
 * Date: 26.08.25
 * Time: 02:59:32
 */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}