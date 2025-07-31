package am.banking.system.user.api.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 31.07.25
 * Time: 23:47:57
 */
@Validated
public record AuthenticationRequest(@NotBlank String username, @NotBlank String password) {
}