package am.banking.system.user.model.repository;

import am.banking.system.common.enums.PermissionEnum;
import am.banking.system.user.model.entity.Permission;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:06:06
 */
@Repository
public interface PermissionRepository extends ReactiveCrudRepository<Permission, Integer> {
    @Query("""
SELECT p.* FROM user_db.usr.permission p
JOIN user_db.usr.role_permission rp ON p.id = rp.permission_id
WHERE rp.role_id = :roleId
""")
    Flux<Permission> findAllByRoleId(Integer roleId);

    @Query("SELECT * FROM user_db.usr.permission WHERE permission_name = :permissionName")
    Mono<Permission> findByPermissionEnum(PermissionEnum permissionName);
}