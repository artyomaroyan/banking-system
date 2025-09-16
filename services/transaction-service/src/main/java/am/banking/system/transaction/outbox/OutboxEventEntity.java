package am.banking.system.transaction.outbox;

import am.banking.system.common.outbox.OutboxStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 27.08.25
 * Time: 01:21:26
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction.outbox_event_entity")
public class OutboxEventEntity {
    @Id
    private UUID id;
    private String topic;
    private String key;
    private String aggregateType;
    private String aggregateId;
    private String type;
    private String payload;
    private OutboxStatus status;
    private Integer tries;
    private String lastError;
    private Instant createdAt;
}