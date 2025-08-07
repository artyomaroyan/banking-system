package am.banking.system.user.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 24.07.25
 * Time: 22:37:27
 */
@Validated
public record PasswordResetEmailRequest(@NotNull UUID userId, @NotBlank String email) {
}