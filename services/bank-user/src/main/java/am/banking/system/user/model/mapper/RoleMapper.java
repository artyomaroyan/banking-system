package am.banking.system.user.model.mapper;

import am.banking.system.user.model.entity.Role;
import am.banking.system.user.model.enums.RoleEnum;
import am.banking.system.user.model.repository.RoleRepository;
import am.banking.system.user.service.permission.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

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

    public Set<Role> getDefaultRole() {
        var defaultRole = RoleEnum.USER;
        return roleRepository.findByRoleName(defaultRole.name())
                .map(Set::of)
                .orElseGet(() -> {
                    var permission = permissionService.getPermissionsByRole(defaultRole);
                    var newRole = new Role(defaultRole, permission);
                    return Set.of(roleRepository.save(newRole));
                });
    }
}