package am.banking.system.common.shared.dto.security;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 21.04.25
 * Time: 00:20:55
 */
@Validated
public record TokenValidatorRequest(@NotBlank String token, @NotBlank String username) {
}