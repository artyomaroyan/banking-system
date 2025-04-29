package am.banking.system.user.model.repository;

import am.banking.system.user.model.entity.Permission;
import am.banking.system.user.model.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:06:06
 */
@Repository
public interface PermissionRepository extends MongoRepository<Permission, Long> {
    Set<Permission> findByRolesContains(Optional<Role> role);
}