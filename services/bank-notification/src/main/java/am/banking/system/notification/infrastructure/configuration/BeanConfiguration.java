package am.banking.system.notification.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 00:16:29
 */
@Configuration
@RequiredArgsConstructor
//@EnableConfigurationProperties(EmailConfiguration.class)
public class BeanConfiguration {
    private final EmailConfiguration mailConfiguration;

    @Bean
    protected JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfiguration.getHost());
        mailSender.setPort(mailConfiguration.getPort());
        mailSender.setUsername(mailConfiguration.getUsername());
        mailSender.setPassword(mailConfiguration.getPassword());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", mailConfiguration.isAuth());
        properties.put("mail.smtp.starttls.enable", mailConfiguration.isEnable());
        properties.put("mail.smtp.starttls.required", mailConfiguration.isRequire());
        properties.put("mail.transport.protocol", mailConfiguration.getProtocol());
        mailSender.setJavaMailProperties(properties);
        return mailSender;


//        Properties properties = mailSender.getJavaMailProperties();
//        properties.put("mail.smtp.auth", mailConfiguration.getProperties().getSmtp().isAuth());
//        properties.put("mail.smtp.starttls.enable", mailConfiguration.getProperties().getSmtp().getStartTls().isEnable());
//        properties.put("mail.smtp.starttls.required", mailConfiguration.getProperties().getSmtp().getStartTls().isRequire());
//        properties.put("mail.transport.protocol", mailConfiguration.getProtocol());
//        mailSender.setJavaMailProperties(properties);
//        return mailSender;
    }
}