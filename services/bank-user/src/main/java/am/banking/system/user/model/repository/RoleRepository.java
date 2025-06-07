package am.banking.system.user.model.repository;

import am.banking.system.user.model.entity.Role;
import am.banking.system.common.enums.RoleEnum;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:05:45
 */
@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {
    Mono<Optional<Role>> findByRoleName(RoleEnum roleEnum);
}