package am.banking.system.user.infrastructure.webclient.configuration;

import am.banking.system.user.application.port.out.security.UserTokenClientPort;
import am.banking.system.user.infrastructure.webclient.BaseUrlProperties;
import am.banking.system.user.infrastructure.webclient.WebClientFactory;
import am.banking.system.user.infrastructure.webclient.TlsProperties;
import am.banking.system.user.infrastructure.webclient.WebClientFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties({BaseUrlProperties.class, TlsProperties.class})
public class UserWebClientConfiguration {
    private final BaseUrlProperties baseUrlProperties;

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient securityWebClient(WebClientFactory webClientFactory, ObjectProvider<UserTokenClientPort> provider) {
        return webClientFactory.createSecuredWebClient(baseUrlProperties.securityBaseUrl())
                .mutate()
                .filter(WebClientFilter.systemTokenPropagationFilter(provider))
                .filter(WebClientFilter.errorResponseFilter())
                .build();
    }

    @Bean
    public WebClient notificationWebClient(WebClientFactory webClientFactory, ObjectProvider<UserTokenClientPort> provider) {
        return webClientFactory.createSecuredWebClient(baseUrlProperties.notificationBaseUrl())
                .mutate()
                .filter(WebClientFilter.systemTokenPropagationFilter(provider))
                .filter(WebClientFilter.errorResponseFilter())
                .build();
    }

    @Bean
    public WebClient accountWebClient(WebClientFactory webClientFactory, ObjectProvider<UserTokenClientPort> provider) {
        return webClientFactory.createSecuredWebClient(baseUrlProperties.accountBaseUrl())
                .mutate()
                .filter(WebClientFilter.systemTokenPropagationFilter(provider))
                .filter(WebClientFilter.errorResponseFilter())
                .build();
    }

    @Bean
    public WebClient transactionWebClient(WebClientFactory webClientFactory,  ObjectProvider<UserTokenClientPort> provider) {
        return webClientFactory.createSecuredWebClient(baseUrlProperties.transactionBaseUrl())
                .mutate()
                .filter(WebClientFilter.systemTokenPropagationFilter(provider))
                .filter(WebClientFilter.errorResponseFilter())
                .build();
    }
}