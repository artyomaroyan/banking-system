package am.banking.system.account.infrastructure.webclient;

import am.banking.system.account.application.port.out.InternalTokenClientPort;
import am.banking.system.common.infrastructure.tls.WebClientFactory;
import am.banking.system.common.infrastructure.tls.WebClientFilter;
import am.banking.system.common.infrastructure.tls.configuration.SecurityTLSProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Author: Artyom Aroyan
 * Date: 16.07.25
 * Time: 22:55:20
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AccountWebClientConfiguration {
    private final SecurityTLSProperties tlsProperties;

    @Bean
    public WebClient securedWebClient(WebClientFactory webClientFactory) {
        return webClientFactory.createSecuredWebClient(tlsProperties.url());
    }

    @Bean(name = "accountWebClient")
    public WebClient accountWebClient(WebClientFactory webClientFactory, ObjectProvider<InternalTokenClientPort> provider) {
        return webClientFactory.createSecuredWebClient(tlsProperties.url())
                .mutate()
                .filter(systemTokenPropagationFilter(provider))
                .filter(WebClientFilter.errorResponseFilter())
                .build();
    }

    private ExchangeFilterFunction systemTokenPropagationFilter(ObjectProvider<InternalTokenClientPort> provider) {
        return ExchangeFilterFunction.ofRequestProcessor(request ->
                provider.getObject()
                        .generateSystemToken()
                        .map(token -> ClientRequest.from(request)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .build()));
    }
}