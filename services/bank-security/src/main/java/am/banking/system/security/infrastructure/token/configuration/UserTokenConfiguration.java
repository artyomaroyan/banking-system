package am.banking.system.security.infrastructure.token.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 01:45:39
 */
@Configuration
@EnableConfigurationProperties(UserTokenProperties.class)
public class UserTokenConfiguration {
}