package am.banking.system.transaction.consumer;

import am.banking.system.common.contracts.event.AccountBalanceChangedV1;
import am.banking.system.transaction.projection.BalanceProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 24.08.25
 * Time: 20:07:58
 */
@Component
@RequiredArgsConstructor
public class AccountBalanceChangedConsumer {
    private final BalanceProjectionRepository balanceProjectionRepository;

    @KafkaListener(
            topics = "account.balance-changed.v1",
            groupId = "transaction-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public Mono<Void> onMessage(AccountBalanceChangedV1 event) {
        return balanceProjectionRepository.upsertIfNewer(
                event.accountId(),
                event.newBalance(),
                event.version(),
                event.occurredAt())
                .then();
    }
}