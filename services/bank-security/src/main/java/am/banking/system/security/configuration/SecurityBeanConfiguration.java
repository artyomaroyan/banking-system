package am.banking.system.security.configuration;

import am.banking.system.security.converter.JwtReactiveAuthenticationConverter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;

/**
 * Author: Artyom Aroyan
 * Date: 14.06.25
 * Time: 20:08:43
 */
@Configuration
public class SecurityBeanConfiguration {
    @Bean
    protected MeterRegistry registry() {
        return new SimpleMeterRegistry();
    }

    @Bean
    protected JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager(
            ReactiveJwtDecoder jwtDecoder, JwtReactiveAuthenticationConverter jwtConverter) {
        var manager = new JwtReactiveAuthenticationManager(jwtDecoder);
        manager.setJwtAuthenticationConverter(jwtConverter);
        return manager;
    }

    @Bean
    protected ReactiveAuthenticationManager authenticationManager(JwtReactiveAuthenticationManager jwtManager) {
        return new DelegatingReactiveAuthenticationManager(jwtManager);
    }
}