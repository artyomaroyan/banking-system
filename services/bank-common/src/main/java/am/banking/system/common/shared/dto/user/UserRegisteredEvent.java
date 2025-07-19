package am.banking.system.common.shared.dto.user;

/**
 * Author: Artyom Aroyan
 * Date: 19.07.25
 * Time: 00:45:09
 */
public record UserRegisteredEvent(Integer userId, String username, String email, String firstName, String lastName, String phone) {
}