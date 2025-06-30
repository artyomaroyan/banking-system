package am.banking.system.user.infrastructure.adapter.out.notification;

import am.banking.system.user.application.port.out.NotificationServiceClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 18:42:18
 */
@Service
@RequiredArgsConstructor
public final class EmailSendingService {
    private final NotificationServiceClientPort notificationServiceClient;

    public Mono<Void> sendWelcomeEmail(String email, String username) {
        return notificationServiceClient.sendWelcomeEmail(email, username);
    }

    public Mono<Void> sendVerificationEmail(String email, String username, String link) {
        return notificationServiceClient.sendVerificationEmail(email, username, link);
    }

    public Mono<Void> sendPasswordResetEmail(String email, String username, String link) {
        return notificationServiceClient.sendPasswordResetEmail(email, username, link);
    }
}