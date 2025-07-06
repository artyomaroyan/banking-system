package am.banking.system.common.shared.dto.security;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 03:43:23
 */
@Validated
public record PasswordHashingRequest(@NotBlank String rawPassword) {
}