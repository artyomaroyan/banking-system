package am.banking.system.user.infrastructure.webclient;

import am.banking.system.common.shared.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 02:47:21
 */
@Slf4j
public final class WebClientFilter {

    private WebClientFilter() {
    }

    public static ExchangeFilterFunction errorResponseFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            HttpStatus status = (HttpStatus) clientResponse.statusCode();
                            log.error("WebClient error: Status code = {}, Reason = {}, Body = {}",
                                    status.value(), status.getReasonPhrase(), errorBody);

                            if (status == HttpStatus.BAD_REQUEST) {
                                return Mono.error(new BadRequestException(errorBody));
                            } else if (status == HttpStatus.UNAUTHORIZED) {
                                return Mono.error(new UnauthorizedException(errorBody));
                            } else if (status == HttpStatus.FORBIDDEN) {
                                return Mono.error(new ForbiddenException(errorBody));
                            } else if (status == HttpStatus.NOT_FOUND) {
                                return Mono.error(new NotFoundException(errorBody));
                            } else if (status.is5xxServerError()) {
                                return Mono.error(new InternalServerErrorException(errorBody));
                            } else {
                                return Mono.error(new WebClientResponseException(
                                        "Unexpected error: " + errorBody,
                                        status.value(),
                                        status.getReasonPhrase(),
                                        clientResponse.headers().asHttpHeaders(),
                                        null,
                                        null
                                ));
                            }
                        });
            }
            return Mono.just(clientResponse);
        });
    }
}