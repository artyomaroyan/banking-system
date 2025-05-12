package am.banking.system.notification.kafka.records;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 02:36:34
 */
public record PasswordReset(String email, String username, String link) {
}