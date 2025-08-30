package am.banking.system.account.consumer;

import am.banking.system.account.application.service.AccountService;
import am.banking.system.common.contracts.event.UserTransferRequestedV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 27.08.25
 * Time: 01:50:33
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserTransferRequestedConsumer {
    private final AccountService accountService;

    @KafkaListener(
            topics = "user.transfer-requested.v1",
            groupId = "account-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public Mono<Void> onUserTransferRequest(UserTransferRequestedV1 requestedV1) {
        return accountService.applyDebit(requestedV1.debitAccountId(), requestedV1.amount())
                .then(accountService.applyCredit(requestedV1.creditAccountId(), requestedV1.amount()))
                .doOnError(error -> log.error("Error applying debit request", error));
    }
}