package am.banking.system.user.mapper.reactive;

import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 20.06.25
 * Time: 18:34:25
 */
@FunctionalInterface
public interface ReactiveMapper<S, T> {
    Mono<T> map(S source);
}