package am.banking.system.notification.kafka.records;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 02:29:46
 */
public record EmailVerification(String email, String username, String link) {
}