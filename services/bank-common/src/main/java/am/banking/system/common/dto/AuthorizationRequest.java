package am.banking.system.common.dto;

import am.banking.system.common.enums.PermissionEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Author: Artyom Aroyan
 * Date: 22.04.25
 * Time: 23:08:38
 */
public record AuthorizationRequest(@NotBlank String token, @NotNull PermissionEnum permission) {
}