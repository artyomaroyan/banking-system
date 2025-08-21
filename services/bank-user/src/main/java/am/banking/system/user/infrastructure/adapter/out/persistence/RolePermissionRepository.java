package am.banking.system.user.infrastructure.adapter.out.persistence;

import am.banking.system.user.domain.model.RolePermission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 11.06.25
 * Time: 15:56:21
 */
@Repository
public interface RolePermissionRepository extends ReactiveCrudRepository<RolePermission, UUID>, RolePermissionCustomRepository {
    Mono<Boolean> existsByRoleIdAndPermissionId(UUID roleId, UUID permissionId);
}