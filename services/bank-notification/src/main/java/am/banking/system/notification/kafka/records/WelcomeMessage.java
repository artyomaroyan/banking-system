package am.banking.system.notification.kafka.records;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 02:35:44
 */
public record WelcomeMessage(String email, String username) {
}