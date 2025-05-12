package am.banking.system.notification.kafka;

import am.banking.system.notification.email.service.EmailService;
import am.banking.system.notification.kafka.records.EmailVerification;
import am.banking.system.notification.model.entity.Notification;
import am.banking.system.notification.model.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static am.banking.system.notification.model.enums.EmailType.*;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 02:25:51
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsConsumer {
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "email-verification")
    public void consumeEmailVerificationNotification(EmailVerification verification) {
        notificationRepository.save(
                Notification.builder()
                        .emailType(EMAIL_VERIFICATION)
                        .notificationDate(LocalDateTime.now())
                        .emailVerification(verification)
                        .build());
        emailService.sendVerificationEmail(verification.email(), verification.username(), verification.link());
    }

    @KafkaListener(topics = "password-recovery")
    public void consumePasswordRecoveryNotification(String email, String username, String link) {
        notificationRepository.save(
                Notification.builder()
                        .emailType(PASSWORD_RECOVERY)
                        .notificationDate(LocalDateTime.now())
                        .build());
        emailService.sendPasswordResetEmail(email, username, link);
    }

    @KafkaListener(topics = "welcome-email")
    public void consumeWelcomeEmailNotification(String email, String username) {
        notificationRepository.save(
                Notification.builder()
                        .emailType(WELCOME_MESSAGE)
                        .notificationDate(LocalDateTime.now())
                        .build());
        emailService.sendWelcomeEmail(email, username);
    }
}