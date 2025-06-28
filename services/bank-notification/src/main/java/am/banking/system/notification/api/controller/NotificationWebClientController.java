package am.banking.system.notification.api.controller;

import am.banking.system.notification.api.dto.EmailVerificationRequest;
import am.banking.system.notification.api.dto.PasswordResetEmailRequest;
import am.banking.system.notification.api.dto.WelcomeEmailRequest;
import am.banking.system.notification.application.service.EmailService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Void> sendVerificationEmail(@Valid @RequestBody EmailVerificationRequest request) {
        emailService.sendVerificationEmail(request.email(), request.username(), request.link());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset")
    public ResponseEntity<Void> sendPasswordResetEmail(@Valid @RequestBody PasswordResetEmailRequest request) {
        emailService.sendPasswordResetEmail(request.email(), request.username(), request.link());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/welcome-email")
    public ResponseEntity<Void> sendWelcomeEmail(@Valid @RequestBody WelcomeEmailRequest request) {
        emailService.sendWelcomeEmail(request.email(), request.username());
        return ResponseEntity.ok().build();
    }
}