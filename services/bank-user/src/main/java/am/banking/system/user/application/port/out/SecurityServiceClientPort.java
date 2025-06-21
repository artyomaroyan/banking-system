package am.banking.system.user.application.port.out;

import am.banking.system.common.shared.enums.PermissionEnum;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:32:21
 */
public interface SecurityServiceClientPort {
    Mono<Boolean> authorizeUser(String token, PermissionEnum permission);
}