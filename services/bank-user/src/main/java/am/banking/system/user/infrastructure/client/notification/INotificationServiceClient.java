package am.banking.system.user.infrastructure.client.notification;

import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 03:29:42
 */
public interface INotificationServiceClient {
    Mono<Void> sendVerificationEmail(String email, String username, String link);
    Mono<Void> sendPasswordResetEmail(String email, String username, String link);
    Mono<Void> sendWelcomeEmail(String email);
}