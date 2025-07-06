package am.banking.system.common.shared.dto.security;

import am.banking.system.common.shared.enums.PermissionEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 03:41:58
 */
@Validated
public record AuthorizationRequest(@NotBlank String token, @NotNull PermissionEnum permission) {
}