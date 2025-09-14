package am.banking.system.common.shared.exception;

/**
 * Author: Artyom Aroyan
 * Date: 12.09.25
 * Time: 13:18:39
 */
public class WebClientResponseException extends RuntimeException {
    public WebClientResponseException(String message) {
        super(message);
    }
}