package am.banking.system.notification.email.service;

import am.banking.system.notification.model.enums.EmailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static am.banking.system.notification.email.enums.EmailTemplate.*;
import static am.banking.system.notification.email.enums.EmailTemplate.EMAIL_VERIFICATION;
import static am.banking.system.notification.model.enums.EmailType.PASSWORD_RECOVERY;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 00:07:58
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private static final String FROM_EMAIL = "banking@system.com";
    private static final String APP_NAME = "Banking System";
    private static final String COMPANY_NAME = "Banking System Inc.";
    private static final String COMPANY_ADDRESS = "Armenia, Yerevan Davtashen 3rd District";
    private static final int EXPIRATION_HOURS = 24;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendVerificationEmail(String to, String username, String verificationLink) {
        Context context = commonContext(username);
        context.setVariable("verificationLink", verificationLink);
        sendEmail(to, EMAIL_VERIFICATION.getSubject(), EMAIL_VERIFICATION.getTemplate(), context, EmailType.EMAIL_VERIFICATION);
    }

    @Async
    public void sendWelcomeEmail(String to, String username) {
        Context context = commonContext(username);
        context.setVariable("dashboardUrl", null);
        context.setVariable("tutorialUrl", "https://localhost:8080/tutorial");
        context.setVariable("faqUrl", "https://localhost:8080/faq");
        context.setVariable("videoTutorialsUrl", "https://localhost:8080/videoTutorials");
        context.setVariable("appStoreUrl", "https://localhost:8080/appStore");
        context.setVariable("playStoreUrl", "https://localhost:8080/playStore");
        context.setVariable("supportEmail", "support@banking-system.com");
        context.setVariable("twitterUrl", "https://twitter.com/banking-system");
        context.setVariable("facebookUrl", "https://facebook.com/banking-system");
        context.setVariable("instagramUrl", "https://instagram.com/banking-system");
        sendEmail(to, WELCOME_MESSAGE.getSubject(), WELCOME_MESSAGE.getTemplate(), context, EmailType.WELCOME_MESSAGE);
    }

    @Async
    public void sendPasswordResetEmail(String to, String username, String resetUrl) {
        Context context = commonContext(username);
        context.setVariable("resetUrl", resetUrl);
        sendEmail(to, PASSWORD_RESET.getSubject(), PASSWORD_RESET.getTemplate(), context, PASSWORD_RECOVERY);
    }

    private void sendEmail(String to, String subject, String templateName, Context context, EmailType type) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
            helper.setFrom(FROM_EMAIL);
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("{} email sent to {}", type, to);
        } catch (MessagingException ex) {
            log.error("Failed to send {} email to {}", type, to, ex);
        }
    }

    private Context commonContext(String username) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("appName", APP_NAME);
        context.setVariable("expirationHours", EXPIRATION_HOURS);
        context.setVariable("companyName", COMPANY_NAME);
        context.setVariable("companyAddress", COMPANY_ADDRESS);
        return context;
    }
}