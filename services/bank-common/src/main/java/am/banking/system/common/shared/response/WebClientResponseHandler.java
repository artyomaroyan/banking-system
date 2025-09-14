package am.banking.system.common.shared.response;

import am.banking.system.common.shared.exception.WebClientResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * Author: Artyom Aroyan
 * Date: 05.07.25
 * Time: 23:29:29
 */
@Slf4j
@Component
public class WebClientResponseHandler {

    public <T> Mono<T> response(ClientResponse response, Class<T> clazz, String context) {
        return handleResponse(response, context, () -> response.bodyToMono(clazz));
    }

    public <T> Mono<T> response(ClientResponse response, ParameterizedTypeReference<T> typeReference, String context) {
        return handleResponse(response, context, () -> response.bodyToMono(typeReference));
    }

    private <T> Mono<T> handleResponse(ClientResponse response, String context, Supplier<Mono<T>> bodySupplier) {
        HttpStatusCode statusCode = response.statusCode();
        String requestInfo = response.request().getMethod().name() + " " + response.request().getURI();

        if (statusCode.is2xxSuccessful()) {
            return bodySupplier.get()
                    .switchIfEmpty(Mono.error(new WebClientResponseException(
                            context + " returned empty body. Request: "  + requestInfo)))
                    .doOnNext(body -> log.info("{} succeeded [{}]: {}", context, requestInfo, body));
        } else {
            return response.bodyToMono(String.class)
                    .defaultIfEmpty("No error body")
                    .flatMap(error -> {
                        log.error("{} failed [{}] - status: {}, body: {}", context, requestInfo, statusCode.value(), error);
                        return Mono.error(new WebClientResponseException(context + " failed ["  + requestInfo + "]: " + statusCode.value() + " - " + error));
                    });
        }
    }
}