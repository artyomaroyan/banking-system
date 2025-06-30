package am.banking.system.notification.infrastructure.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 00:16:29
 */
@Configuration
//@EnableConfigurationProperties(NotificationProperties.class)
public class BeanConfiguration {
    @Bean
    protected JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }
}