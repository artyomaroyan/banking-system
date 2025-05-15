package am.banking.system.notification.kafka;

import am.banking.system.notification.email.service.EmailService;
import am.banking.system.notification.kafka.dto.EmailVerification;
import am.banking.system.notification.kafka.dto.PasswordReset;
import am.banking.system.notification.kafka.dto.WelcomeEmail;
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

    @KafkaListener(topics = "email-verification", groupId = "notification-service-group")
    public void consumeEmailVerificationNotification(EmailVerification request) {
        notificationRepository.save(
                Notification.builder()
                        .emailType(EMAIL_VERIFICATION)
                        .notificationDate(LocalDateTime.now())
                        .emailVerification(request)
                        .build());
        emailService.sendVerificationEmail(request.email(), request.username(), request.link());
    }

    @KafkaListener(topics = "password-recovery", groupId = "notification-service-group")
    public void consumePasswordRecoveryNotification(PasswordReset request) {
        notificationRepository.save(
                Notification.builder()
                        .emailType(PASSWORD_RECOVERY)
                        .notificationDate(LocalDateTime.now())
                        .passwordReset(request)
                        .build());
        emailService.sendPasswordResetEmail(request.email(), request.username(), request.link());
    }

    @KafkaListener(topics = "welcome-email", groupId = "notification-service-group")
    public void consumeWelcomeEmailNotification(WelcomeEmail request) {
        notificationRepository.save(
                Notification.builder()
                        .emailType(WELCOME_MESSAGE)
                        .notificationDate(LocalDateTime.now())
                        .welcomeEmail(request)
                        .build());
        emailService.sendWelcomeEmail(request.email(), request.username());
    }
}