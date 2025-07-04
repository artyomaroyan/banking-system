package am.banking.system.user.domain.repository;

import reactor.core.publisher.Flux;

/**
 * Author: Artyom Aroyan
 * Date: 20.06.25
 * Time: 20:01:51
 */
public interface UserRoleCustomRepository {
    Flux<String> findRolesByUserId(Integer userId);
}