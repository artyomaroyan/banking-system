package am.banking.system.account.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

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
public class OutboxEvent {
    @Id
    private UUID id;
    private String topic;
    private String key;
    private String payload;
    private String header;
    private Instant createdAt;
    private boolean published;
}