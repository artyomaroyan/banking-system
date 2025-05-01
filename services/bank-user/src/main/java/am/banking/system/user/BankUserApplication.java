package am.banking.system.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"am.banking.system.user", "am.banking.system.common"})
public class BankUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankUserApplication.class, args);
    }
}