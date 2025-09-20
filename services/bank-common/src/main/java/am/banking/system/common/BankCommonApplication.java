package am.banking.system.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class BankCommonApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankCommonApplication.class, args);
    }
}