package am.banking.system.user.db;

import am.banking.system.common.enums.PermissionEnum;
import am.banking.system.common.enums.RoleEnum;
import am.banking.system.user.model.entity.Permission;
import am.banking.system.user.model.entity.Role;
import am.banking.system.user.model.repository.PermissionRepository;
import am.banking.system.user.model.repository.RoleRepository;
import am.banking.system.user.service.permission.RolePermissionLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static am.banking.system.common.enums.PermissionEnum.*;
import static am.banking.system.common.enums.RoleEnum.*;

/**
 * Author: Artyom Aroyan
 * Date: 16.05.25
 * Time: 17:28:59
 */
@Slf4j
@Component
@RequiredArgsConstructor
@DependsOn("flywayInitializer")
class RolePermissionInitializer {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionLinkService rolePermissionLinkService;

    @Bean
    public CommandLineRunner initializePermissionsAndRoles() {
        return _ -> initializePermissions()
                .then(initializeRoles())
                .doOnError(e -> log.error("Failed to initialize permissions and roles", e))
                .onErrorResume(e -> {
                    log.warn("Role/Permission initialization skipped due to error: {}", e.getMessage());
                 return Mono.empty();
                })
                .subscribe();
    }

    private Mono<Void> initializePermissions() {
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
                DO_INTERNAL_TASKS
        );

        return Flux.fromIterable(permissionEnums)
                .flatMap(permissionName -> permissionRepository.findByPermissionEnum(permissionName)
                        .switchIfEmpty(permissionRepository.save(new Permission(permissionName))))
                .then(); // Return Mono<Void>
    }

    private Mono<Void> initializeRoles() {
        Map<RoleEnum, Set<PermissionEnum>> rolePermission = Map.of(

                SYSTEM, Set.of(DO_INTERNAL_TASKS),

                GUEST, Set.of(VIEW_PUBLIC_INFO, REGISTER_ACCOUNT, LOGIN),

                USER, Set.of(VIEW_PUBLIC_INFO, LOGOUT, VIEW_OWN_PROFILE, UPDATE_OWN_PROFILE, OPEN_ACCOUNT,
                        VIEW_OWN_ACCOUNTS, MAKE_TRANSACTION, VIEW_OWN_TRANSACTIONS, VIEW_OWN_NOTIFICATIONS),

                MANAGER, Set.of(VIEW_PUBLIC_INFO, LOGOUT, VIEW_OWN_PROFILE, UPDATE_OWN_PROFILE,
                        VIEW_ALL_USERS, OPEN_ACCOUNT, VIEW_OWN_ACCOUNTS, VIEW_ALL_ACCOUNTS, FREEZE_ACCOUNT,
                        UNFREEZE_ACCOUNT, CLOSE_ACCOUNT, MAKE_TRANSACTION, VIEW_OWN_TRANSACTIONS, VIEW_ALL_TRANSACTIONS,
                        APPROVE_LARGE_TRANSACTION, VIEW_OWN_NOTIFICATIONS),

                ADMIN, Set.of(VIEW_PUBLIC_INFO, LOGOUT, VIEW_OWN_PROFILE, UPDATE_OWN_PROFILE, VIEW_ALL_USERS,
                        UPDATE_ANY_USER, DELETE_USER, OPEN_ACCOUNT, VIEW_OWN_ACCOUNTS, VIEW_ALL_ACCOUNTS, FREEZE_ACCOUNT,
                        UNFREEZE_ACCOUNT, CLOSE_ACCOUNT, MAKE_TRANSACTION, VIEW_OWN_TRANSACTIONS, VIEW_ALL_TRANSACTIONS,
                        APPROVE_LARGE_TRANSACTION, ROLLBACK_TRANSACTION, VIEW_ROLES_AND_PERMISSIONS, ASSIGN_ROLES,
                        MANAGE_PERMISSIONS, VIEW_OWN_NOTIFICATIONS, VIEW_AUDIT_LOGS));

        return Flux.fromIterable(rolePermission.entrySet())
                .flatMap(entry -> {
                    RoleEnum roleName = entry.getKey();
                    Set<PermissionEnum> permissionEnums = entry.getValue();

                    return Flux.fromIterable(permissionEnums)
                            .flatMap(permissionRepository::findByPermissionEnum)
                            .collect(Collectors.toSet())

                            .flatMap(permissions ->
                                    roleRepository.findByRoleName(roleName)
                                    .switchIfEmpty(roleRepository.save(new Role(roleName)))

                                    .flatMap(role -> {
                                        Mono<Role> savedRole = role.getId() == null ?
                                                roleRepository.save(role) :
                                                Mono.just(role);
                                        return savedRole.flatMap(r ->
                                                rolePermissionLinkService.savePermissionsForRole(r, permissions));
                                            }));
                })
                .then();
    }
}