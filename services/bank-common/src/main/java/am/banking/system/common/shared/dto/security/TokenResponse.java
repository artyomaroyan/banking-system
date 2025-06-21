package am.banking.system.common.shared.dto.security;

import jakarta.validation.constraints.NotBlank;

/**
 * Author: Artyom Aroyan
 * Date: 21.04.25
 * Time: 00:09:27
 */
public record TokenResponse(@NotBlank String token) {
}