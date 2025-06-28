package am.banking.system.notification.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 28.06.25
 * Time: 23:07:37
 */
@Validated
public record PasswordResetEmailRequest(
        @Email String email, @NotBlank String username, @NotBlank String link) {
}