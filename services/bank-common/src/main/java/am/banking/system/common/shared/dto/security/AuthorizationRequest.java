package am.banking.system.common.shared.dto.security;

import am.banking.system.common.shared.enums.PermissionEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 03:41:58
 */
public record AuthorizationRequest(@NotBlank String token, @NotNull PermissionEnum permission) {
}