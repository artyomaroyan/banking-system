package am.banking.system.user.domain.service.access;

import am.banking.system.user.domain.entity.Permission;
import am.banking.system.user.domain.entity.Role;
import am.banking.system.user.domain.entity.RolePermission;
import am.banking.system.user.domain.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

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
                                .filter(exists -> !exists)
                                .flatMap(_ -> rolePermissionRepository.save(
                                        RolePermission.of(role.getId(), permission.getId()))
                                ))
                .then();
    }

    public Mono<Void> assignPermissionsToRole(UUID roleId, Set<Permission> permissions) {
        return Flux.fromIterable(permissions)
                .flatMap(permission -> rolePermissionRepository.save(
                        RolePermission.of(roleId, permission.getId())
                ))
                .then();
    }
}