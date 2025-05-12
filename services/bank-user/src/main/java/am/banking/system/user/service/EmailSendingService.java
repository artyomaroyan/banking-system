package am.banking.system.user.service;

import am.banking.system.user.infrastructure.notification.abstraction.INotificationServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 18:42:18
 */
@Service
@RequiredArgsConstructor
public final class EmailSendingService {
    private final INotificationServiceClient notificationServiceClient;

    public void sendWelcomeEmail(String email) {
        notificationServiceClient.sendWelcomeEmail(email);
    }

    public void sendVerificationEmail(String email, String username, String link) {
        notificationServiceClient.sendVerificationEmail(email, username, link);
    }

    public void sendPasswordResetEmail(String email, String username, String link) {
        notificationServiceClient.sendPasswordResetEmail(email, username, link);
    }
}