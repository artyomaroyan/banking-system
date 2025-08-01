package am.banking.system.common.shared.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Set;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 15:57:31
 */
@Validated
public record UserDto(@NotNull UUID userId, @NotBlank String username, @NotBlank String email,
                      @NotEmpty Set<String> roles, @NotEmpty Set<String> permissions) {
}