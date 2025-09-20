package am.banking.system.security;

import am.banking.system.common.infrastructure.configuration.InternalSecretProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(InternalSecretProperties.class)
public class BankSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankSecurityApplication.class, args);
    }
}