package am.banking.system.common.ssl.exception;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 01:04:49
 */
public class WebClientSslContextException extends RuntimeException {
    public WebClientSslContextException(String message, Throwable cause) {
        super(message, cause);
    }
}