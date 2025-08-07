package am.banking.system.common.shared.dto.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.04.25
 * Time: 00:20:55
 */
@Validated
public record TokenValidatorRequest(@NotNull UUID userId, @NotBlank String token, @NotBlank String username) {
}