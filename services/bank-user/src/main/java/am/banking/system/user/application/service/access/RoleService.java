package am.banking.system.user.application.service.access;

import am.banking.system.common.shared.enums.RoleEnum;
import am.banking.system.user.domain.model.Role;
import am.banking.system.user.infrastructure.adapter.out.persistence.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

import static am.banking.system.common.shared.enums.RoleEnum.USER;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 01:59:20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;
    private final RolePermissionLinkService rolePermissionLinkService;

    public Mono<Set<Role>> getDefaultRole() {
        var defaultRole = USER;
        log.info("Fetching default roles: {}", defaultRole);
        return roleRepository.findByRoleName(defaultRole)
                .map(Set::of)
                .doOnNext(role -> log.info("Found existing default role: {}", role))
                .switchIfEmpty(
                        createRoleWithDefaultPermissions(defaultRole)
                                .doOnNext(role -> log.info("Created default role: {}", role))
                                .map(Set::of)
                );
    }

    private Mono<Role> createRoleWithDefaultPermissions(RoleEnum roleName) {
        log.debug("Creating role with default permissions for role {}", roleName);
        return permissionService.getPermissionIdsByRole(roleName)
                .collect(Collectors.toSet())
                .flatMap(permissionIds -> {
                    Role newRole = new Role(roleName);

                    return roleRepository.save(newRole)
                            .flatMap(savedRole ->
                                    rolePermissionLinkService.assignPermissionsToRole(savedRole.getId(), permissionIds)
                                            .thenReturn(savedRole));
                });
    }
}