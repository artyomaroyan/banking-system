package am.banking.system.common.configuration;

import am.banking.system.common.infrastructure.tls.WebClientFactory;
import am.banking.system.common.infrastructure.tls.configuration.InternalSecretProperties;
import am.banking.system.common.infrastructure.tls.configuration.SecurityTLSProperties;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

/**
 * Author: Artyom Aroyan
 * Date: 02.06.25
 * Time: 22:48:36
 */
@Configuration
@EnableConfigurationProperties({SecurityTLSProperties.class, InternalSecretProperties.class})
public class CommonBeanConfiguration {

    @Bean
    public WebClientFactory webClientFactory(SecurityTLSProperties securityTLSProperties) {
        return new WebClientFactory(securityTLSProperties);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(STRICT);
        return modelMapper;
    }
}