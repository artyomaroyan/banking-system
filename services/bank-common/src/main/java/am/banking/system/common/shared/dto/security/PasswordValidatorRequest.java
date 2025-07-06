package am.banking.system.common.shared.dto.security;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 21.04.25
 * Time: 00:06:56
 */
@Validated
public record PasswordValidatorRequest(@NotBlank String rawPassword, @NotBlank String hashedPassword) {
}