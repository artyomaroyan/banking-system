package am.banking.system.user.model.mapper;

import am.banking.system.common.dto.user.UserDto;
import am.banking.system.user.model.dto.UserRequest;
import am.banking.system.user.model.dto.UserResponse;
import am.banking.system.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 01:18:33
 */
@Component
@RequiredArgsConstructor
public class UserMapper {
    private final UserFactory userFactory;
    private final ModelMapper modelMapper;

    public Mono<User> mapFromRequestToEntity(UserRequest request) {
        return userFactory.createUser(request);
    }

    public UserDto mapFromEntityToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public UserResponse mapFromEntityToResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
}