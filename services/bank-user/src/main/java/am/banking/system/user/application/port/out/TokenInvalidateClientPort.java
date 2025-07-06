package am.banking.system.user.application.port.out;

import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 06.07.25
 * Time: 15:30:47
 */
public interface TokenInvalidateClientPort {
    Mono<Void> invalidateUsedToken(String token);
}