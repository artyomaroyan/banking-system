package am.banking.system.user.service.permission;

import am.banking.system.user.model.entity.Permission;
import am.banking.system.common.enums.RoleEnum;
import am.banking.system.user.model.repository.PermissionRepository;
import am.banking.system.user.model.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

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

    public Set<Permission> getPermissionsByRole(RoleEnum roleEnum) {
        var role = roleRepository.findByRoleName(roleEnum);
        return permissionRepository.findByRolesContains(role);
    }
}