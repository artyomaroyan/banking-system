package am.banking.system.transaction.infrastructure.adapter.in.kafka;

import am.banking.system.common.messages.FundsReservedEvent;
import am.banking.system.transaction.domain.enums.Status;
import am.banking.system.transaction.domain.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;

/**
 * Author: Artyom Aroyan
 * Date: 08.09.25
 * Time: 22:25:30
 */
@Component
@RequiredArgsConstructor
public class AccountsEventsConsumer {
    private final ObjectMapper objectMapper;
    private final TransactionRepository transactionRepository;
    private final KafkaReceiver<String, String> kafkaReceiver;

    public void start() {
        kafkaReceiver.receive()
                .flatMap(record -> {
                    var payload = record.value();
                    try {
                        var event = objectMapper.readValue(payload, FundsReservedEvent.class);
                        return handleFundsReserved(event)
                                .then(Mono.fromRunnable(record.receiverOffset()::acknowledge));
                    } catch (Exception ex) {
                        return Mono.error(ex);
                    }
                })
                .subscribe();
    }

    private Mono<Void> handleFundsReserved(FundsReservedEvent event) {
        var transferId = event.transferId();
        return transactionRepository.findById(transferId)
                .flatMap(transaction -> {
                    var updated = transaction.toBuilder()
                            .reservationId(event.reservationId())
                            .status(Status.RESERVED)
                            .build();
                    return transactionRepository.save(updated);
                })
                .then();
    }
}