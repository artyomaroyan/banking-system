package am.banking.system.common.tls.exception;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 01:04:49
 */
public class WebClientTLSContextException extends RuntimeException {
    public WebClientTLSContextException(String message, Throwable cause) {
        super(message, cause);
    }
}