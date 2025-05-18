package am.banking.system.security.token.configuration;

import am.banking.system.security.token.key.provider.ECKeyStoreManager;
import am.banking.system.security.token.key.provider.JwtTokenKeyProvider;
import am.banking.system.security.token.strategy.KeyProviderStrategy;
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