package am.banking.system.account;

import am.banking.system.common.configuration.CommonBeanConfiguration;
import am.banking.system.common.configuration.KafkaReactiveConfiguration;
import am.banking.system.common.util.GenericMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({GenericMapper.class, CommonBeanConfiguration.class, KafkaReactiveConfiguration.class})
public class BankAccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankAccountApplication.class, args);
    }
}