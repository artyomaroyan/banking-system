package am.banking.system.user.model.repository;

import am.banking.system.common.enums.PermissionEnum;
import am.banking.system.user.model.entity.Permission;
import am.banking.system.user.model.entity.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:06:06
 */
@Repository
public interface PermissionRepository extends ReactiveCrudRepository<Permission, Long> {
    Set<Permission> findByRolesContains(Optional<Role> role);

    Optional<Permission> findByPermissionName(PermissionEnum permissionName);
}