package am.banking.system.common.tls.configuration;

import am.banking.system.common.tls.WebClientFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 23:45:15
 */
@Configuration
@EnableConfigurationProperties({SecurityTLSProperties.class, InternalSecretProperties.class})
public class BeanConfiguration {
    @Bean
    public WebClientFactory webClientFactory(SecurityTLSProperties securityTLSProperties) {
        return new WebClientFactory(securityTLSProperties);
    }
}