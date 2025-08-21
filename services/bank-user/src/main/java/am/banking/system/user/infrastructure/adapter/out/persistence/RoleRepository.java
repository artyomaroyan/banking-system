package am.banking.system.user.infrastructure.adapter.out.persistence;

import am.banking.system.common.shared.enums.RoleEnum;
import am.banking.system.user.domain.model.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:05:45
 */
@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, UUID> {
    Mono<Role> findByRoleName(RoleEnum roleName);
}