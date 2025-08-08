package am.banking.system.common.shared.dto.security;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 08.08.25
 * Time: 23:20:44
 */
@Validated
public record AuthenticationRequest(@NotBlank String username, @NotBlank String password) {
}