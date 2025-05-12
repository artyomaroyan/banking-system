package am.banking.system.common.dto.notification;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 19:26:04
 */
public record EmailRequest(String email, String username, String link) {
}