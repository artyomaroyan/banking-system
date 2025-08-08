package am.banking.system.common.shared.model;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 07.08.25
 * Time: 01:12:19
 */
@Component
public class BaseEntityCallback implements BeforeConvertCallback<BaseEntity> {

    @Override
    public Mono<BaseEntity> onBeforeConvert(BaseEntity entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        return Mono.just(entity);
    }
}