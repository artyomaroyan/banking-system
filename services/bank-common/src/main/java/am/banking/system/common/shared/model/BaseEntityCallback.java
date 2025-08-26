package am.banking.system.common.shared.model;

import lombok.NonNull;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 07.08.25
 * Time: 01:12:19
 */
@Component
public class BaseEntityCallback<T extends BaseEntity> implements BeforeConvertCallback<T> {

    @NonNull
    @Override
    public Mono<T> onBeforeConvert(@NonNull T entity, @NonNull SqlIdentifier table) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        return Mono.just(entity);
    }
}