package am.banking.system.user.infrastructure.notification.client;

import am.banking.system.common.dto.notification.EmailRequest;
import am.banking.system.user.infrastructure.notification.abstraction.INotificationServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 03:25:31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceClient implements INotificationServiceClient {
    private final WebClient webClient;

    @Retry(name = "notificationService")
    @CircuitBreaker(name = "notificationService")
    @Override
    public void sendVerificationEmail(String email, String username, String link) {
        EmailRequest request = new EmailRequest(email, username, link);
        sendNotification("/api/notification/email-verification", request);
    }

    @Retry(name = "notificationService")
    @CircuitBreaker(name = "notificationService")
    @Override
    public void sendPasswordResetEmail(String email, String username, String link) {
        EmailRequest request = new EmailRequest(email, username, link);
        sendNotification("/api/notification/password-reset", request);
    }

    @Retry(name = "notificationService")
    @CircuitBreaker(name = "notificationService")
    @Override
    public void sendWelcomeEmail(String email) {
        EmailRequest request = new EmailRequest(email, null, null);
        sendNotification("/api/notification/welcome-email", request);
    }

    private void sendNotification(String uri, EmailRequest request) {
        webClient.post()
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> log.error("Failed to send notification to {}", request.email(), error))
                .subscribe();
    }
}