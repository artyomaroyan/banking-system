package am.banking.system.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"am.banking.system.notification", "am.banking.system.common"})
public class BankNotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankNotificationApplication.class, args);
    }
}