package am.banking.system.security.model.repository;

import am.banking.system.security.model.entity.UserToken;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author: Artyom Aroyan
 * Date: 16.04.25
 * Time: 23:29:04
 */
@Repository
public interface UserTokenRepository extends ReactiveCrudRepository<UserToken, Long> {
    Optional<UserToken> findByToken(String token);

//    @Modifying
//    @Query("update UserToken ut set ut.tokenState = 'FORCIBLY_EXPIRED' where ut.tokenState = 'PENDING' and ut.expirationDate < CURRENT_TIMESTAMP")
//    int markTokensForciblyExpired();
}