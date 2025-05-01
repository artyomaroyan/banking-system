package am.banking.system.security.configuration;

import am.banking.system.common.ssl.WebClientFactory;
import am.banking.system.security.exception.*;
import am.banking.system.security.model.dto.UserPrincipal;
import am.banking.system.security.token.service.abstraction.IJwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Author: Artyom Aroyan
 * Date: 20.04.25
 * Time: 00:49:55
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityWebClientConfiguration {
    private final IJwtTokenService jwtTokenService;

    @Bean
    public WebClient internalWebClient() {
        return WebClient.builder()
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .filter(errorResponseFilter())
                .build();
    }

    @Bean
    public WebClient securedWebClient(WebClientFactory webClientFactory) {
        return webClientFactory.createSecuredWebClient()
                .mutate()
                .filter(jwtTokenPropagationFilter())
                .filter(errorResponseFilter())
                .build();
    }

    private ExchangeFilterFunction jwtTokenPropagationFilter() {
        return (request, next) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() &&
                    authentication.getPrincipal() instanceof UserPrincipal user) {
                String jwt = jwtTokenService.generateJwtToken(user);
                request.headers().set(AUTHORIZATION, "Bearer " + jwt);
            }
            return next.exchange(request);
        };
    }

    private ExchangeFilterFunction errorResponseFilter() {
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