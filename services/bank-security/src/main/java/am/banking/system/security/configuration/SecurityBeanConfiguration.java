package am.banking.system.security.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;

import java.util.List;

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

    // If you have multiple authentication strategies (JWT, DB, etc.) you should use this Bean.
    @Bean
    protected ReactiveAuthenticationManager authenticationManager(List<ReactiveAuthenticationManager> managers) {
        return new DelegatingReactiveAuthenticationManager(managers);

    }
    // If you’re using ReactiveUserDetailsService for fetching users from DB: you should use this Bean.
//    @Bean
//    protected ReactiveAuthenticationManager authenticationManager(
//            ReactiveUserDetailsService userDetailsService, Argon2PasswordEncoder passwordEncoder) {
//
//        UserDetailsRepositoryReactiveAuthenticationManager manager =
//                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
//        manager.setPasswordEncoder(passwordEncoder);
//        return manager;
//    }
}