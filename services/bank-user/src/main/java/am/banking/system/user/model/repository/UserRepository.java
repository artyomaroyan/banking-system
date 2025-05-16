package am.banking.system.user.model.repository;

import am.banking.system.user.model.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:04:45
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, Long> {
    Mono<Boolean> existsByUsername(String username);

    Mono<Boolean> existsByEmail(String email);

    Mono<User> findUserByUsername(String username);

    Mono<User> findUserByEmail(String email);
}