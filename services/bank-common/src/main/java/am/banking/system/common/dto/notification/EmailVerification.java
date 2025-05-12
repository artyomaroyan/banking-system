package am.banking.system.common.dto.notification;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 03:37:45
 */
public record EmailVerification(String email, String username, String link) {
}