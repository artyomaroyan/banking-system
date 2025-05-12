package am.banking.system.common.dto.security;

import jakarta.validation.constraints.NotBlank;

/**
 * Author: Artyom Aroyan
 * Date: 21.04.25
 * Time: 00:06:56
 */
public record PasswordValidatorRequest(@NotBlank String rawPassword, @NotBlank String hashedPassword) {
}