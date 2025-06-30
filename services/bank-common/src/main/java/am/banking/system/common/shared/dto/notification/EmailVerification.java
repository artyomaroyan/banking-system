package am.banking.system.common.shared.dto.notification;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 15.05.25
 * Time: 13:38:42
 */
@Validated
public record EmailVerification(@Email String email, @NotBlank String username, @NotBlank String link) {
}