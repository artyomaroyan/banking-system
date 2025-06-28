package am.banking.system.notification.application.port.out;

import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 27.06.25
 * Time: 01:24:09
 */
public interface JwtTokenServiceClientPort {
    Mono<String> generateSystemToken();
}