package am.banking.system.notification.application.service;

import am.banking.system.common.shared.exception.notification.EmailSendingException;
import am.banking.system.notification.domain.enums.EmailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static am.banking.system.notification.domain.enums.EmailTemplate.*;
import static am.banking.system.notification.domain.enums.EmailType.PASSWORD_RECOVERY;
import static java.nio.charset.StandardCharsets.UTF_8;

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
    private static final int EXPIRATION_HOURS = 15;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendVerificationEmail(String to, String username, String verificationLink) {
        try {
            Context context = commonContext(username);
            context.setVariable("verificationLink", verificationLink);
            sendEmail(to, EMAIL_VERIFICATION.getSubject(), EMAIL_VERIFICATION.getTemplate(), context, EmailType.EMAIL_VERIFICATION);
        } catch (EmailSendingException ex) {
            throw new EmailSendingException("Filed to send verification email" + ex.getMessage());
        }
    }

    public void sendWelcomeEmail(String to, String username) {
        try {
            Context context = commonContext(username);
            context.setVariable("dashboardUrl", "Replace with real dashboard if exists.");
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
        } catch (EmailSendingException ex) {
            throw new EmailSendingException("Filed to send welcome email" + ex.getMessage());
        }
    }

    public void sendPasswordResetEmail(String to, String username, String resetLink) {
        try {
            Context context = commonContext(username);
            context.setVariable("resetLink", resetLink);
            sendEmail(to, PASSWORD_RESET.getSubject(), PASSWORD_RESET.getTemplate(), context, PASSWORD_RECOVERY);
        } catch (EmailSendingException ex) {
            throw new EmailSendingException("Filed to send password reset email" + ex.getMessage());
        }
    }

    private void sendEmail(String to, String subject, String templateName, Context context, EmailType type) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8.name());
            String htmlContent = templateEngine.process(templateName, context);

            helper.setFrom(FROM_EMAIL);
            helper.setTo(to);
            helper.setSubject(subject);
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