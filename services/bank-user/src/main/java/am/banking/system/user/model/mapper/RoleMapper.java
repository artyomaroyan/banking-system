package am.banking.system.user.model.mapper;

import am.banking.system.common.enums.PermissionEnum;
import am.banking.system.user.model.entity.Permission;
import am.banking.system.user.model.entity.Role;
import am.banking.system.user.model.repository.PermissionRepository;
import am.banking.system.user.model.repository.RoleRepository;
import am.banking.system.user.service.permission.RolePermissionLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

import static am.banking.system.common.enums.PermissionEnum.*;
import static am.banking.system.common.enums.RoleEnum.USER;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 01:59:20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionLinkService rolePermissionLinkService;

    public Mono<Set<Role>> getDefaultRole() {
        return roleRepository.findByRoleName(USER)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("USER role not found in DB. Initializing default roles...");
                    return initializeDefaultRoles().then(roleRepository.findByRoleName(USER));
                }))
                .map(Set::of)
                .doOnNext(roles -> log.info("Using default roles {}", roles));
    }

    private Mono<Void> initializeDefaultRoles() {
        return roleRepository.findByRoleName(USER)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("USER role not found in DB. Creating default role with basic permissions...");

                    Set<PermissionEnum> userPermissions = Set.of(
                            VIEW_PUBLIC_INFO, LOGOUT, VIEW_OWN_PROFILE, UPDATE_OWN_PROFILE, OPEN_ACCOUNT,
                            VIEW_OWN_ACCOUNTS, MAKE_TRANSACTION, VIEW_OWN_TRANSACTIONS, VIEW_OWN_NOTIFICATIONS
                    );

                    return roleRepository.save(new Role(USER))
                            .flatMap(savedRole ->
                                    Flux.fromIterable(userPermissions)
                            .flatMap(permissionName ->
                                    permissionRepository.findByPermissionEnum(permissionName)
                                            .switchIfEmpty(Mono.defer(() -> {
                                                log.warn("Permission {} not found in DB. Creating it", permissionName);
                                                return permissionRepository.save(new Permission(permissionName));
                                            }))
                            )
                            .collectList()
                            .flatMap(permissions -> {
                                if (permissions.isEmpty()) {
                                    log.warn("No permissions found/created for USER role");
                                    return  Mono.error(new RuntimeException("Failed to initialize USER role permissions"));
                                }
                                return rolePermissionLinkService.savePermissionsForRole(savedRole, new HashSet<>(permissions)

                                );
                            })
                            )
                    .then(Mono.fromRunnable(() ->
                            log.info("Successfully initialized default USER role with {} permissions", userPermissions.size())
                    ));
                }))
                .then();
    }
}