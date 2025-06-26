package am.banking.system.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Author: Artyom Aroyan
 * Date: 26.06.25
 * Time: 18:11:23
 */
@Slf4j
@Configuration
public class StartupLogger {

    public StartupLogger(Environment environment) {
        log.info("Password reset token loaded: {}",
                environment.getProperty("spring.application.token.access.password-recovery.secret"));
        log.info("Email verification token loaded: {}",
                environment.getProperty("spring.application.token.access.email-verification.secret"));
    }
}