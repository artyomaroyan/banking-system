package am.banking.system.user.infrastructure.repository.implementation;

import am.banking.system.user.model.repository.RolePermissionCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Author: Artyom Aroyan
 * Date: 20.06.25
 * Time: 21:43:42
 */
@Repository
@RequiredArgsConstructor
public class RolePermissionRepositoryImpl implements RolePermissionCustomRepository {
    private final DatabaseClient databaseClient;

    @Override
    public Flux<String> findRolePermissionsByRoleName(String roleName) {
        return databaseClient.sql(
                        """
                                SELECT p.permission_name
                                FROM user_db.usr.role r
                                JOIN user_db.usr.role_permission rp ON r.id = rp.role_id
                                JOIN user_db.usr.permission p ON rp.permission_id = p.id
                                WHERE r.role_name = :roleName
                                """
                )
                .bind("roleName", roleName)
                .map(row -> row.get("permission_name", String.class))
                .all();
    }
}