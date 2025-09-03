//package am.banking.system.transaction.application.service;
//
//import am.banking.system.common.contracts.event.UserTransferRequestedV1;
//import am.banking.system.transaction.outbox.OutboxEvent;
//import am.banking.system.transaction.outbox.OutboxRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
///**
// * Author: Artyom Aroyan
// * Date: 27.08.25
// * Time: 01:45:51
// */
//@Service
//@RequiredArgsConstructor
//public class TransferService {
//    private final ObjectMapper objectMapper;
//    private final OutboxRepository outboxRepository;
//
//    public void handleTransfer(UserTransferRequestedV1 requestedV1) {
//        OutboxEvent event = OutboxEvent.from(
//                "Transaction",
//                requestedV1.userId(),
//                "UserTransferRequestedV1",
//                requestedV1,
//                objectMapper
//        );
//        outboxRepository.save(event).subscribe();
//    }
//}