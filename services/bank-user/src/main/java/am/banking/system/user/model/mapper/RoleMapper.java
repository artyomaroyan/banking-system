package am.banking.system.user.model.mapper;

import am.banking.system.user.model.entity.Role;
import am.banking.system.user.model.repository.RoleRepository;
import am.banking.system.user.service.permission.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

import static am.banking.system.common.enums.RoleEnum.USER;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 01:59:20
 */
@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    public Mono<Set<Role>> getDefaultRole() {
        var defaultRole = USER;
        return roleRepository.findByRoleName(defaultRole)
                .map(Set::of)
                .switchIfEmpty(permissionService.getPermissionsByRole(defaultRole)
                        .flatMap(_ -> {
                            Role newRole = new Role(defaultRole);
                            return roleRepository.save(newRole).map(Set::of);
                        })
                );
    }
}