package am.banking.system.transaction.outbox;

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
 * Date: 27.08.25
 * Time: 01:21:26
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction.outbox_event")
public class OutboxEvent {
    @Id
    private UUID id;
    private String topic;
    private String key;
    private String payload;
    private String header;
    private Instant createdAt;
    private boolean published;

//    public static OutboxEvent from(String aggregateType, UUID aggregateId, String type, Object payload, ObjectMapper mapper) {
//        try {
//            return new OutboxEvent(
//                    UUID.randomUUID(),
//                    aggregateId,
//                    aggregateType,
//                    type,
//                    mapper.writeValueAsString(payload),
//                    Instant.now(),
//                    false
//            );
//        } catch (JsonProcessingException ex) {
//            throw new RuntimeException("Failed to serialize event", ex);
//        }
//    }
}