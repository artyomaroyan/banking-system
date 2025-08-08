package am.banking.system.common.shared.model;

import org.springframework.data.mapping.callback.EntityCallback;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 07.08.25
 * Time: 01:26:41
 */
@FunctionalInterface
public interface BeforeConvertCallback<T> extends EntityCallback<T> {
    Mono<T> onBeforeConvert(T entity);
}