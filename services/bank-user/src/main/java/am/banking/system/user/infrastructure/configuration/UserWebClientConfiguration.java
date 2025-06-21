package am.banking.system.user.infrastructure.configuration;

import am.banking.system.common.infrastructure.tls.WebClientFactory;
import am.banking.system.common.infrastructure.tls.configuration.SecurityTLSProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:27:13
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserWebClientConfiguration {
    private final SecurityTLSProperties tlsProperties;

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient securedWebClient(WebClientFactory webClientFactory) {
        return webClientFactory.createSecuredWebClient(tlsProperties.url());
    }

    @Bean
    public WebClient notificationWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8040")
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter(notificationExchangeFilter())
                .build();
    }

    private ExchangeFilterFunction notificationExchangeFilter() {
        return (request, next) -> next.exchange(request);
    }
}