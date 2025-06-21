package am.banking.system.user.application.mapper;

import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.user.domain.entity.User;
import am.banking.system.user.domain.repository.RolePermissionRepository;
import am.banking.system.user.domain.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 20.06.25
 * Time: 18:35:43
 */
@Component
@RequiredArgsConstructor
public class UserDtoMapper implements ReactiveMapper<User, UserDto> {
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    public Mono<UserDto> map(User source) {
        return userRoleRepository.findRolesByUserId(source.getId())
                .collect(Collectors.toSet())
                .flatMap(roles -> Flux.fromIterable(roles)
                        .flatMap(rolePermissionRepository::findRolePermissionsByRoleName)
                        .collect(Collectors.toSet())
                        .map(permissions -> new UserDto(
                                source.getId(),
                                source.getUsername(),
                                source.getPassword(),
                                source.getEmail(),
                                roles,
                                permissions
                        )));
    }
}