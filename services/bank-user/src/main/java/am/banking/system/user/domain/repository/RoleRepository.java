package am.banking.system.user.domain.repository;

import am.banking.system.common.shared.enums.RoleEnum;
import am.banking.system.user.domain.entity.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:05:45
 */
@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, Integer> {
    Mono<Role> findByRoleName(RoleEnum roleName);
}