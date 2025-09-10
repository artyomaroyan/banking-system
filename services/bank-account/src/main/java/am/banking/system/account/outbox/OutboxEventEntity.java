package am.banking.system.account.outbox;

import am.banking.system.common.shared.outbox.OutboxStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 23.08.25
 * Time: 01:49:01
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("account.outbox_event")
public class OutboxEventEntity {
    @Id
    private UUID id;
    private String aggregateType;
    private String aggregateId;
    private String type;
    private String payload;
    private OutboxStatus status;
    private Instant createdAt;
}