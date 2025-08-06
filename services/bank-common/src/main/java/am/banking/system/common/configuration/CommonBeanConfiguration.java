package am.banking.system.common.configuration;

import am.banking.system.common.infrastructure.configuration.InternalSecretProperties;
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
@EnableConfigurationProperties({InternalSecretProperties.class})
public class CommonBeanConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(STRICT);
        return modelMapper;
    }
}