package am.banking.system.user.domain.repository;

import am.banking.system.common.shared.enums.PermissionEnum;
import am.banking.system.common.shared.enums.RoleEnum;
import am.banking.system.user.domain.entity.Permission;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:06:06
 */
@Repository
public interface PermissionRepository extends ReactiveCrudRepository<Permission, UUID> {
    @Query("""
SELECT p.* FROM user_db.usr.permission p
JOIN user_db.usr.role_permission rp ON p.id = rp.permission_id
WHERE rp.role_id = :roleId
""")
    Flux<Permission> findAllByRoleId(Integer roleId);

    @Query("SELECT * FROM user_db.usr.permission WHERE permission_name = :permissionName")
    Mono<Permission> findByPermissionEnum(PermissionEnum permissionName);

    @Query("""
SELECT p.* FROM user_db.usr.permission p
JOIN user_db.usr.role_permission rp on p.id = rp.permission_id
JOIN user_db.usr.role r on rp.role_id = r.id
WHERE r.role_name = :roleName
""")
    Flux<Permission> findPermissionByRole(@Param("roleName") RoleEnum roleName);
}