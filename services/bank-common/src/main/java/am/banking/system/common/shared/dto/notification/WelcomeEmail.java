package am.banking.system.common.shared.dto.notification;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 15.05.25
 * Time: 13:41:39
 */
@Validated
public record WelcomeEmail(@Email String email, @NotBlank String username) {
}