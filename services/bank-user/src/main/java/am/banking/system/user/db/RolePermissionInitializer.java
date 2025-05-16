package am.banking.system.user.db;

import am.banking.system.common.enums.PermissionEnum;
import am.banking.system.user.model.entity.Permission;
import am.banking.system.user.model.entity.Role;
import am.banking.system.user.model.enums.RoleEnum;
import am.banking.system.user.model.repository.PermissionRepository;
import am.banking.system.user.model.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static am.banking.system.common.enums.PermissionEnum.*;
import static am.banking.system.user.model.enums.RoleEnum.*;

/**
 * Author: Artyom Aroyan
 * Date: 16.05.25
 * Time: 17:28:59
 */
@Component
@RequiredArgsConstructor
final class RolePermissionInitializer {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @PostConstruct
    void initialize() {
        initializePermissions();
        initializeRoles();
    }

    private void initializeRoles() {
        Map<RoleEnum, Set<PermissionEnum>> rolePermission = Map.of(
                GUEST, Set.of(VIEW_PUBLIC_INFO, REGISTER_ACCOUNT, LOGIN),

                USER, Set.of(VIEW_PUBLIC_INFO, LOGIN, LOGOUT, VIEW_OWN_PROFILE, UPDATE_OWN_PROFILE, OPEN_ACCOUNT,
                        VIEW_OWN_ACCOUNTS, CLOSE_ACCOUNT, MAKE_TRANSACTION, VIEW_OWN_TRANSACTIONS, VIEW_OWN_NOTIFICATIONS),

                MANAGER, Set.of(VIEW_PUBLIC_INFO, LOGIN, LOGOUT, VIEW_OWN_PROFILE, UPDATE_OWN_PROFILE,
                        VIEW_ALL_USERS, OPEN_ACCOUNT, VIEW_OWN_ACCOUNTS, VIEW_ALL_ACCOUNTS, FREEZE_ACCOUNT,
                        UNFREEZE_ACCOUNT, CLOSE_ACCOUNT, MAKE_TRANSACTION, VIEW_OWN_TRANSACTIONS, VIEW_ALL_TRANSACTIONS,
                        APPROVE_LARGE_TRANSACTION, VIEW_OWN_NOTIFICATIONS),

                ADMIN, Set.of(VIEW_PUBLIC_INFO, LOGIN, LOGOUT, VIEW_OWN_PROFILE, UPDATE_OWN_PROFILE, VIEW_ALL_USERS,
                        UPDATE_ANY_USER, DELETE_USER, OPEN_ACCOUNT, VIEW_OWN_ACCOUNTS, VIEW_ALL_ACCOUNTS, FREEZE_ACCOUNT,
                        UNFREEZE_ACCOUNT, CLOSE_ACCOUNT, MAKE_TRANSACTION, VIEW_OWN_TRANSACTIONS, VIEW_ALL_TRANSACTIONS,
                        APPROVE_LARGE_TRANSACTION, ROLLBACK_TRANSACTION, VIEW_ROLES_AND_PERMISSIONS, ASSIGN_ROLES,
                        MANAGE_PERMISSIONS, VIEW_OWN_NOTIFICATIONS, VIEW_AUDIT_LOGS),

                SYSTEM, Set.of(GENERATE_SYSTEM_TOKEN));

        Set<PermissionEnum> allPermissions = rolePermission.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        Map<PermissionEnum, Permission> permissionEntity = allPermissions.stream()
                .map(permissionRepository::findByPermissionName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Permission::getPermissionName,
                        permission -> permission,
                        (existing, _) -> existing));

        rolePermission.forEach((roleEnum, permissionEnum) -> {
            Role existingRole = roleRepository.findByRoleName(roleEnum.name()).orElse(null);

            Set<Permission> permissionForRole = permissionEnum.stream()
                    .map(permissionEntity::get)
                    .collect(Collectors.toSet());

            if (existingRole == null) {
                roleRepository.save(new Role(roleEnum, permissionForRole));
            } else {
                Set<Permission> existingPermission = existingRole.getPermissions();
                if (existingPermission.containsAll(permissionForRole)) {
                    existingRole.setPermissions(permissionForRole);
                    roleRepository.save(existingRole);
                }
            }
        });
    }

    private void initializePermissions() {
        Set<PermissionEnum> permissionEnums = Set.of(
                // AUTHENTICATION
                VIEW_PUBLIC_INFO, REGISTER_ACCOUNT, LOGIN, LOGOUT,

                // USER PROFILE
                VIEW_OWN_PROFILE, UPDATE_OWN_PROFILE,
                VIEW_ALL_USERS, UPDATE_ANY_USER, DELETE_USER,

                // ACCOUNTS
                OPEN_ACCOUNT, VIEW_OWN_ACCOUNTS, VIEW_ALL_ACCOUNTS,
                FREEZE_ACCOUNT, UNFREEZE_ACCOUNT, CLOSE_ACCOUNT,

                // TRANSACTIONS
                MAKE_TRANSACTION, VIEW_OWN_TRANSACTIONS,
                VIEW_ALL_TRANSACTIONS, APPROVE_LARGE_TRANSACTION, ROLLBACK_TRANSACTION,

                // ROLES & PERMISSIONS
                VIEW_ROLES_AND_PERMISSIONS, ASSIGN_ROLES, MANAGE_PERMISSIONS,

                // NOTIFICATIONS & AUDIT
                VIEW_OWN_NOTIFICATIONS, VIEW_AUDIT_LOGS,

                // SYSTEM
                GENERATE_SYSTEM_TOKEN
        );

        permissionEnums.forEach(permission ->
                permissionRepository.findByPermissionName(permission)
                        .orElseGet(() -> permissionRepository.save(new Permission(permission))));
    }
}