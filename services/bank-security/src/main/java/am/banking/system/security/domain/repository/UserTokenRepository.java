package am.banking.system.security.domain.repository;

import am.banking.system.security.domain.model.UserToken;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author: Artyom Aroyan
 * Date: 16.04.25
 * Time: 23:29:04
 */
@Repository
public interface UserTokenRepository extends ReactiveCrudRepository<UserToken, Integer> {
    Optional<UserToken> findByToken(String token);
}