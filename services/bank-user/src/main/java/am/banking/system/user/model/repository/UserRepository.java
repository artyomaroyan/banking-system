package am.banking.system.user.model.repository;

import am.banking.system.user.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:04:45
 */
@Repository
public interface UserRepository extends MongoRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);
}