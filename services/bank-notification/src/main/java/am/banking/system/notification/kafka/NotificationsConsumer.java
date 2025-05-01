package am.banking.system.notification.kafka;

import am.banking.system.notification.email.service.EmailService;
import am.banking.system.notification.kafka.records.EmailVerification;
import am.banking.system.notification.model.entity.Notification;
import am.banking.system.notification.model.enums.NotificationType;
import am.banking.system.notification.model.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static am.banking.system.notification.model.enums.NotificationType.EMAIL_VERIFICATION;

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
                        .notificationType(EMAIL_VERIFICATION)
                        .notificationDate(LocalDateTime.now())
                        .emailVerification(verification)
                        .build());
        emailService.sendVerificationEmail(verification.email(), verification.username(), null);
    }
}