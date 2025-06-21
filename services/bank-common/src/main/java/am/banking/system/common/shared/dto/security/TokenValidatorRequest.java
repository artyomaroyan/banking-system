package am.banking.system.common.shared.dto.security;

import jakarta.validation.constraints.NotBlank;

/**
 * Author: Artyom Aroyan
 * Date: 21.04.25
 * Time: 00:20:55
 */
public record TokenValidatorRequest(@NotBlank String token, @NotBlank String username) {
}