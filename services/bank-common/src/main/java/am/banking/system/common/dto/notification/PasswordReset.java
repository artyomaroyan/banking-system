package am.banking.system.common.dto.notification;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 03:39:34
 */
public record PasswordReset(String email, String username, String link) {
}