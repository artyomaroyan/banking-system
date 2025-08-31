package am.banking.system.user.infrastructure.adapter.out.transaction;

import am.banking.system.common.shared.response.WebClientResponseHandler;
import am.banking.system.user.application.port.out.security.UserTokenClientPort;
import am.banking.system.user.application.port.out.transaction.TransactionClientPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Author: Artyom Aroyan
 * Date: 31.08.25
 * Time: 16:49:16
 */
@Slf4j
@Service
public class TransactionClient implements TransactionClientPort {
    private final WebClient webClient;
    private final UserTokenClientPort userTokenClient;
    private final WebClientResponseHandler webClientResponseHandler;

    public TransactionClient(@Qualifier("transactionWebClient") WebClient webClient,
                             UserTokenClientPort userTokenClient, WebClientResponseHandler webClientResponseHandler) {
        this.webClient = webClient;
        this.userTokenClient = userTokenClient;
        this.webClientResponseHandler = webClientResponseHandler;
    }


}