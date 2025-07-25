package am.banking.system.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {"am.banking.system.security", "am.banking.system.common"})
public class BankSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankSecurityApplication.class, args);
    }
}