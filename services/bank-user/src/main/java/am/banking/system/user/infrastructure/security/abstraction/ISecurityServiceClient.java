package am.banking.system.user.infrastructure.security.abstraction;

import am.banking.system.common.enums.PermissionEnum;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:32:21
 */
public interface ISecurityServiceClient {
    Mono<Boolean> authorizeUser(String token, PermissionEnum permission);
}