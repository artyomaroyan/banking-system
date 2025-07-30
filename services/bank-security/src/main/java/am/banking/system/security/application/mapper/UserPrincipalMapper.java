package am.banking.system.security.application.mapper;

import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.security.api.shared.UserPrincipal;
import org.springframework.stereotype.Component;

/**
 * Author: Artyom Aroyan
 * Date: 02.07.25
 * Time: 14:12:22
 */
@Component
public class UserPrincipalMapper {
    public static UserPrincipal toUserPrincipal(UserDto userDto) {
        return new UserPrincipal(
                userDto.userId(),
                userDto.username(),
                null,
                userDto.email(),
                userDto.roles(),
                userDto.permissions()
        );
    }
}