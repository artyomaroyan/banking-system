package am.banking.system.user.service.permission;

import am.banking.system.user.model.entity.Permission;
import am.banking.system.user.model.entity.Role;
import am.banking.system.user.model.entity.RolePermission;
import am.banking.system.user.model.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
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
        List<RolePermission> links = permissions.stream()
                .map(permission -> new RolePermission(role.getId(), permission.getId()))
                .toList();
        return rolePermissionRepository.saveAll(links).then();
    }
}