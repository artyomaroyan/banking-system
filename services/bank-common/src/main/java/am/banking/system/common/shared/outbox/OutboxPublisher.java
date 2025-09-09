package am.banking.system.common.shared.outbox;

/**
 * Author: Artyom Aroyan
 * Date: 09.09.25
 * Time: 18:23:52
 */
public interface OutboxPublisher {
    void publish(OutboxEvent event);
}