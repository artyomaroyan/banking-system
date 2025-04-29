package am.banking.system.common.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Author: Artyom Aroyan
 * Date: 21.04.25
 * Time: 00:02:27
 */
public record PasswordHashingRequest(@NotBlank String password) {
}