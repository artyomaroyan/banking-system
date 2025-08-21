package am.banking.system.user.infrastructure.adapter.out.persistence;

import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 20.06.25
 * Time: 20:01:51
 */
public interface UserRoleCustomRepository {
    Flux<String> findRolesByUserId(UUID userId);
}