package am.banking.system.user.infrastructure.notification.client;

import am.banking.system.common.dto.notification.EmailVerification;
import am.banking.system.common.dto.notification.PasswordReset;
import am.banking.system.common.dto.notification.WelcomeMessage;
import am.banking.system.user.infrastructure.notification.abstraction.INotificationServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 03:25:31
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceClient implements INotificationServiceClient {
    private final WebClient webClient;

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Void sendEmailVerificationEmail(EmailVerification email) {
        return webClient.post()
                .uri("/api/notification/email-verification")
                .bodyValue(email)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Void sendPasswordResetEmail(PasswordReset email) {
        return webClient.post()
                .uri("/api/notification/password-reset")
                .bodyValue(email)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Retry(name = "securityService")
    @CircuitBreaker(name = "securityService")
    @Override
    public Void sendWelcomeEmail(WelcomeMessage email) {
        return webClient.post()
                .uri("/api/notification/welcome-email")
                .bodyValue(email)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}