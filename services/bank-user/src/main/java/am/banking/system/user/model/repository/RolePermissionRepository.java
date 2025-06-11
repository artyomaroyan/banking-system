package am.banking.system.user.model.repository;

import am.banking.system.user.model.entity.RolePermission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Artyom Aroyan
 * Date: 11.06.25
 * Time: 15:56:21
 */
@Repository
public interface RolePermissionRepository extends ReactiveCrudRepository<RolePermission, Long> {
}