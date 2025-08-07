package am.banking.system.user.domain.service.access;

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

//    public Mono<Void> savePermissionsForRole(UUID roleId, Set<UUID> permissionIds) {
//        return Flux.fromIterable(permissionIds)
//                .flatMap(permissionId ->
//                        rolePermissionRepository.save(new RolePermission(roleId, permissionId))
//                                .then()).then();
//    }
//
//    public Mono<Void> assignPermissionsToRole(UUID roleId, Set<UUID> permissionIds) {
//        return Flux.fromIterable(permissionIds)
//                .flatMap(permissionId ->
//                        rolePermissionRepository.save(new RolePermission(roleId, permissionId)
//                        ))
//                .then();
//    }

//----------------------------------------------------------------------------------------------------------------------

    public Mono<Void> savePermissionsForRole(UUID roleId, Set<UUID> permissionIds) {
        return Flux.fromIterable(permissionIds)
                .flatMap(permissionId ->
                        rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)
                                .filter(exists -> !exists)
                                .flatMap(_ ->
                                        rolePermissionRepository.save(new RolePermission(roleId, permissionId))
                                ))
                .then();
    }

    public Mono<Void> assignPermissionsToRole(UUID roleId, Set<UUID> permissionIds) {
        return Flux.fromIterable(permissionIds)
                .flatMap(permissionId ->
                        rolePermissionRepository.save(new RolePermission(roleId, permissionId)
                        ))
                .then();
    }
}