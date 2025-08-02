package am.banking.system.user.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 28.07.25
 * Time: 15:50:56
 */
@Validated
public record PasswordResetConfirmRequest(@NotNull UUID userId, @NotBlank String resetToken, @NotBlank String newPassword) {
}