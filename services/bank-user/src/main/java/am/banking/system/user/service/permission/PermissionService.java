package am.banking.system.user.service.permission;

import am.banking.system.user.model.entity.Permission;
import am.banking.system.common.enums.RoleEnum;
import am.banking.system.user.model.entity.Role;
import am.banking.system.user.model.repository.PermissionRepository;
import am.banking.system.user.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 24.04.25
 * Time: 00:48:24
 */
@Service
@RequiredArgsConstructor
public class PermissionService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public Mono<Set<Permission>> getPermissionsByRole(RoleEnum roleEnum) {
        return roleRepository.findByRoleName(roleEnum)
                .map(Role::getId)
                .flatMapMany(permissionRepository::findAllByRoleId)
                .collect(Collectors.toSet());
    }
}