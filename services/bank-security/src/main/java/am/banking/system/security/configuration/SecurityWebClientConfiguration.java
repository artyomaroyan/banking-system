package am.banking.system.security.configuration;

import am.banking.system.common.tls.WebClientFactory;
import am.banking.system.common.tls.configuration.SecurityTLSProperties;
import am.banking.system.security.token.service.abstraction.IJwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static am.banking.system.common.tls.util.CommonWebClientFilter.errorResponseFilter;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Author: Artyom Aroyan
 * Date: 20.04.25
 * Time: 00:49:55
 */
@Configuration
@RequiredArgsConstructor
public class SecurityWebClientConfiguration {
    private final SecurityTLSProperties tlsProperties;
    private final IJwtTokenService iJwtTokenService;

    @Bean(name = "internalWebClient")
    public WebClient internalWebClient() {
        return WebClient.builder()
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .filter(errorResponseFilter())
                .build();
    }

    @Bean(name = "securedWebClient")
    public WebClient securedWebClient(WebClientFactory webClientFactory) {
        return webClientFactory.createSecuredWebClient(tlsProperties.url())
                .mutate()
                .filter(systemTokenPropagationFilter())
                .filter(errorResponseFilter())
                .build();
    }

    private ExchangeFilterFunction systemTokenPropagationFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(request ->
                Mono.just(ClientRequest.from(request)
                        .header(AUTHORIZATION, "Bearer ", iJwtTokenService.generateSystemToken())
                        .build()));
    }

//    private ExchangeFilterFunction jwtTokenPropagationFilter() {
//        return (request, next) -> {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication != null && authentication.isAuthenticated() &&
//                    authentication.getPrincipal() instanceof UserPrincipal user) {
//                String jwt = jwtTokenService.generateJwtToken(user);
//                request.headers().set(AUTHORIZATION, "Bearer " + jwt);
//            }
//            return next.exchange(request);
//        };
//    }
}