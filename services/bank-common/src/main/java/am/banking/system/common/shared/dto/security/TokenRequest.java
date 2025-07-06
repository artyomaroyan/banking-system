package am.banking.system.common.shared.dto.security;

import am.banking.system.common.shared.dto.user.UserDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 05.07.25
 * Time: 17:09:05
 */
@Validated
public record TokenRequest(@NotNull UserDto userDto) {
}