package am.banking.system.user.model.repository;

import reactor.core.publisher.Flux;

/**
 * Author: Artyom Aroyan
 * Date: 20.06.25
 * Time: 21:29:19
 */
public interface RolePermissionCustomRepository {
    Flux<String> findRolePermissionsByRoleName(String roleName);
}