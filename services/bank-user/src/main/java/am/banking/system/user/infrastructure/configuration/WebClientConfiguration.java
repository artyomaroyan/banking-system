package am.banking.system.user.infrastructure.configuration;

import am.banking.system.common.tls.WebClientFactory;
import am.banking.system.common.tls.configuration.SecurityTLSProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:27:13
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {
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
}