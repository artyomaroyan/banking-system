package am.banking.system.common.ssl.configuration;

import am.banking.system.common.ssl.WebClientFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 23:45:15
 */
@Configuration
@EnableConfigurationProperties(SecuritySSLProperties.class)
public class BeanConfiguration {
    @Bean
    public WebClientFactory webClientFactory(SecuritySSLProperties securitySSLProperties) {
        return new WebClientFactory(securitySSLProperties);
    }
}