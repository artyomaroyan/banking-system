package am.banking.system.notification.email.controller;

import am.banking.system.notification.email.service.EmailService;
import am.banking.system.notification.kafka.records.EmailVerification;
import am.banking.system.notification.kafka.records.PasswordReset;
import am.banking.system.notification.kafka.records.WelcomeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 01:58:29
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationWebClientController {
    private final EmailService emailService;

    @PostMapping("/email-verification")
    public ResponseEntity<Void> sendVerificationEmail(@RequestBody EmailVerification verification) {
        emailService.sendVerificationEmail(verification.email(), verification.username(), verification.link());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset")
    public ResponseEntity<Void> sendPasswordResetEmail(@RequestBody PasswordReset reset) {
        emailService.sendPasswordResetEmail(reset.email(), reset.username(), reset.link());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/welcome-email")
    public ResponseEntity<Void> sendWelcomeEmail(@RequestBody WelcomeMessage message) {
        emailService.sendWelcomeEmail(message.email(), message.username(), null);
        return ResponseEntity.ok().build();
    }
}