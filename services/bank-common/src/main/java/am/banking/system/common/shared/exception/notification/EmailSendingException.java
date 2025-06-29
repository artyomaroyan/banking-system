package am.banking.system.common.shared.exception.notification;

/**
 * Author: Artyom Aroyan
 * Date: 29.06.25
 * Time: 17:56:55
 */
public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message) {
        super(message);
    }
}