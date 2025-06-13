package am.banking.system.user.service.permission;

import am.banking.system.user.model.entity.Permission;
import am.banking.system.user.model.entity.Role;
import am.banking.system.user.model.entity.RolePermission;
import am.banking.system.user.model.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 11.06.25
 * Time: 15:57:26
 */
@Component
@RequiredArgsConstructor
public class RolePermissionLinkService {
    private final RolePermissionRepository rolePermissionRepository;

    public Mono<Void> savePermissionsForRole(Role role, Set<Permission> permissions) {
        return Flux.fromIterable(permissions)
                .flatMap(permission ->
                        rolePermissionRepository.existsByRoleIdAndPermissionId(role.getId(), permission.getId())
                                .flatMap(exists -> {
                                    if (Boolean.FALSE.equals(exists)) {
                                        return rolePermissionRepository.save(
                                                new RolePermission(role.getId(), permission.getId()));
                                    }
                                    return  Mono.empty();
                                }))
                .then();
    }
}