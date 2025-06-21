package am.banking.system.security.infrastructure.token.configuration;

import am.banking.system.security.infrastructure.token.key.ECKeyStoreManager;
import am.banking.system.security.infrastructure.token.key.JwtTokenKeyProvider;
import am.banking.system.security.application.strategy.KeyProviderStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Artyom Aroyan
 * Date: 18.05.25
 * Time: 21:22:35
 */
@Configuration
public class JwtKeyProviderConfiguration {

    @Bean
    public KeyProviderStrategy jwtKeyProviderStrategy(JwtTokenProperties properties, ECKeyStoreManager manager) {
        return JwtTokenKeyProvider.fromProperties(properties, manager);
    }
}