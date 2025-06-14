package am.banking.system.security.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}