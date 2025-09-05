package am.banking.system.user.infrastructure.adapter.out.notification;

import am.banking.system.common.shared.dto.notification.EmailVerification;
import am.banking.system.common.shared.dto.notification.PasswordReset;
import am.banking.system.common.shared.dto.notification.WelcomeEmail;
import am.banking.system.user.application.port.out.notification.NotificationClientPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 03:25:31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationClient implements NotificationClientPort {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Retry(name = "notificationService")
    @CircuitBreaker(name = "notificationService")
    public Mono<Void> sendVerificationEmail(String email, String username, String link) {
        EmailVerification request = new EmailVerification(email, username, link);
        return sendKafkaMessage("email-verification", request);
    }

    @Override
    @Retry(name = "notificationService")
    @CircuitBreaker(name = "notificationService")
    public Mono<Void> sendPasswordResetEmail(String email, String username, String link) {
        PasswordReset request = new PasswordReset(email, username, link);
        return sendKafkaMessage("password-reset", request);
    }

    @Override
    @Retry(name = "notificationService")
    @CircuitBreaker(name = "notificationService")
    public Mono<Void> sendWelcomeEmail(String email,  String username) {
        WelcomeEmail request = new WelcomeEmail(email, username);
        return sendKafkaMessage("welcome-email", request);
    }

    private Mono<Void> sendKafkaMessage(String topic, Object message) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);
        return Mono.fromFuture(future)
                .doOnNext(_ -> log.info("Kafka message sent to topic: '{}' : {}", topic, message))
                .doOnError(error ->
                        log.error("Failed to sent kafka message to topic: '{}' : {}", topic, error.getMessage(), error))
                .then();
    }
}