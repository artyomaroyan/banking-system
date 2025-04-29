package am.banking.system.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 23:07:16
 */
public record UserDto(@NotNull Long userId, @NotBlank String username, @NotBlank String password,
                      @NotBlank String email, @NotBlank Set<String> roles, @NotBlank Set<String> permissions) {
}