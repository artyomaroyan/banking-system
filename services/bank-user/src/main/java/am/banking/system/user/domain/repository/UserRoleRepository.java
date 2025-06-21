package am.banking.system.user.domain.repository;

import am.banking.system.user.domain.entity.UserRole;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Artyom Aroyan
 * Date: 18.06.25
 * Time: 00:25:41
 */
@Repository
public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, Integer>, UserRoleCustomRepository {
}