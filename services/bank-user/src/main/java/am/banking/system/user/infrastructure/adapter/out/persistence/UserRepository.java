package am.banking.system.user.infrastructure.adapter.out.persistence;

import am.banking.system.user.domain.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:04:45
 */
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {
    Mono<Boolean> existsByUsername(String username);

    Mono<Boolean> existsByEmail(String email);

    Mono<User> findUserByUsername(String username);

    Mono<User> findUserByEmail(String email);
}