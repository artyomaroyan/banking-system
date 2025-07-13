package am.banking.system.notification.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 00:16:29
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MailConfiguration.class)
public class BeanConfiguration {
    private final MailConfiguration mailConfiguration;

    @Bean
    protected JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfiguration.host());
        mailSender.setPort(mailConfiguration.port());
        mailSender.setUsername(mailConfiguration.username());
        mailSender.setPassword(mailConfiguration.password());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", mailConfiguration.auth());
        properties.put("mail.smtp.starttls.enable", mailConfiguration.enable());
        properties.put("mail.smtp.starttls.required", mailConfiguration.require());
        properties.put("mail.transport.protocol", mailConfiguration.protocol());
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }

    @Bean
    protected KafkaListenerErrorHandler kafkaListenerErrorHandler() {
        return (message, exception) -> {
            log.error("Kafka listener error: {}", exception.getMessage(), exception);
            return message;
        };
    }
}