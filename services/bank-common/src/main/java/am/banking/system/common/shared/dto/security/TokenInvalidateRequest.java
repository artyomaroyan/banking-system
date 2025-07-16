package am.banking.system.common.shared.dto.security;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 16.07.25
 * Time: 15:49:24
 */
@Validated
public record TokenInvalidateRequest(@NotBlank String token) {
}