package am.banking.system.security.infrastructure.password;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

/**
 * Author: Artyom Aroyan
 * Date: 05.07.25
 * Time: 14:12:26
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(Argon2Properties.class)
public class PasswordEncoderConfiguration {
    private final Argon2Properties properties;

    @Bean
    public Argon2PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(
                properties.saltLength(),
                properties.hashLength(),
                properties.parallelism(),
                properties.memory(),
                properties.iterations()
        );
    }
}