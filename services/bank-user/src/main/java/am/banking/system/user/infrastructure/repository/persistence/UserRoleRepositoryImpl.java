package am.banking.system.user.infrastructure.repository.persistence;

import am.banking.system.user.domain.repository.UserRoleCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Author: Artyom Aroyan
 * Date: 20.06.25
 * Time: 21:57:44
 */
@Repository
@RequiredArgsConstructor
public class UserRoleRepositoryImpl implements UserRoleCustomRepository {
    private final DatabaseClient databaseClient;

    @Override
    public Flux<String> findRolesByUserId(Integer userId) {
        return databaseClient.sql("""
                        SELECT r.role_name
                        FROM user_db.usr.user_role ur
                        JOIN user_db.usr.role r ON ur.role_id = r.id
                        WHERE ur.user_id = :userId
                        """)
                .bind("userId", userId)
                .map(row -> row.get("role_name", String.class))
                .all();
    }
}