package am.banking.system.user;

import am.banking.system.common.configuration.CommonBeanConfiguration;
import am.banking.system.common.shared.response.WebClientResponseHandler;
import am.banking.system.common.util.GenericMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({CommonBeanConfiguration.class, WebClientResponseHandler.class, GenericMapper.class})
public class BankUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankUserApplication.class, args);
    }
}