package am.banking.system.account.consumer;

import am.banking.system.account.application.service.AccountService;
import am.banking.system.common.contracts.event.UserTransferRequestedV1;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Author: Artyom Aroyan
 * Date: 27.08.25
 * Time: 01:50:33
 */
@Component
@RequiredArgsConstructor
public class UserTransferRequestedConsumer {
    private final AccountService accountService;

    @KafkaListener(
            topics = "user.transfer-requested.v1",
            groupId = "account-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onUserTransferRequest(UserTransferRequestedV1 requestedV1) {
        accountService.applyDebit(requestedV1.debitAccountId(), requestedV1.amount())
                .then(accountService.applyCredit(requestedV1.creditAccountId(), requestedV1.amount()))
                .subscribe();
    }
}