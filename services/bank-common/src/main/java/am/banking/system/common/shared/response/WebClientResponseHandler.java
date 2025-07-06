package am.banking.system.common.shared.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 05.07.25
 * Time: 23:29:29
 */
@Slf4j
@Component
public class WebClientResponseHandler {

    public <T> Mono<T> response(ClientResponse response, Class<T> clazz, String context) {
        HttpStatusCode statusCode = response.statusCode();

        if (statusCode.is2xxSuccessful()) {
            return response.bodyToMono(clazz)
                    .switchIfEmpty(Mono.error(new RuntimeException(context + " returned empty body")))
                    .doOnNext(body -> log.info("{} response: {}", context, body));
        } else {
            return response.bodyToMono(String.class)
                    .defaultIfEmpty("No error body")
                    .flatMap(error -> {
                        log.error("{} failed - status: {}, body: {}",  context, statusCode.value(), error);
                        return Mono.error(new RuntimeException(context + " failed: " + statusCode.value() + " - " + error));
                    });
        }
    }
}