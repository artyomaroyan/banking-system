package am.banking.system.notification.infrastructure.kafka;

import am.banking.system.common.shared.dto.notification.EmailVerification;
import am.banking.system.common.shared.dto.notification.PasswordReset;
import am.banking.system.common.shared.dto.notification.WelcomeEmail;
import am.banking.system.notification.application.service.EmailService;
import am.banking.system.notification.domain.entity.Notification;
import am.banking.system.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static am.banking.system.notification.domain.enums.EmailType.*;

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

    @KafkaListener(topics = "email-verification", groupId = "notification-service-group", errorHandler = "kafkaListenerErrorHandler")
    public void consumeEmailVerificationNotification(EmailVerification request) {
        log.info("Received kafka event for email verification : {}", request.email());

        try {
            emailService.sendVerificationEmail(request.email(), request.username(), request.link());

            notificationRepository.save(
                    Notification.builder()
                            .emailType(EMAIL_VERIFICATION)
                            .notificationDate(LocalDateTime.now())
                            .recipientEmail(request.email())
                            .username(request.username())
                            .verificationLink(request.link())
                            .build()
            )
                    .subscribe(saved -> log.info("Notification saved for email: {}", saved.getRecipientEmail()),
                            error -> log.error("Error saving notification: {}", error.getMessage(), error));
        } catch (Exception e) {
            log.error("Error in consumeEmailVerificationNotification for {}: {}",  request.email(), e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "password-reset", groupId = "notification-service-group", errorHandler = "kafkaListenerErrorHandler")
    public void consumePasswordRecoveryNotification(PasswordReset request) {
        notificationRepository.save(
                        Notification.builder()
                                .emailType(PASSWORD_RECOVERY)
                                .notificationDate(LocalDateTime.now())
                                .recipientEmail(request.email())
                                .username(request.username())
                                .verificationLink(request.link())
                                .build())
                .subscribe();
        emailService.sendPasswordResetEmail(request.email(), request.username(), request.link());
    }

    @KafkaListener(topics = "welcome-email", groupId = "notification-service-group", errorHandler = "kafkaListenerErrorHandler")
    public void consumeWelcomeEmailNotification(WelcomeEmail request) {
        notificationRepository.save(
                        Notification.builder()
                                .emailType(WELCOME_MESSAGE)
                                .notificationDate(LocalDateTime.now())
                                .recipientEmail(request.email())
                                .username(request.username())
                                .build())
                .subscribe();
        emailService.sendWelcomeEmail(request.email(), request.username());
    }
}