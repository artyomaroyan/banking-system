package am.banking.system.account.application.port.out;

import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 16.07.25
 * Time: 23:05:22
 */
public interface InternalTokenClientPort {
    Mono<String> generateSystemToken();
}