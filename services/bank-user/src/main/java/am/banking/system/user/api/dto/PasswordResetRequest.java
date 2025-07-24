package am.banking.system.user.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Author: Artyom Aroyan
 * Date: 24.07.25
 * Time: 22:37:27
 */
public record PasswordResetRequest(@NotNull Integer userId, @NotBlank String email,
                                   @NotBlank String resetToken, @NotBlank String newPassword) {
}