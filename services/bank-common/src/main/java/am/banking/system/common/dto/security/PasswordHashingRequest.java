package am.banking.system.common.dto.security;

import jakarta.validation.constraints.NotBlank;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 03:43:23
 */
public record PasswordHashingRequest(@NotBlank String password) {
}