package am.banking.system.notification.email.enums;

import lombok.Getter;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 00:08:38
 */
public enum EmailTemplate {
    EMAIL_VERIFICATION("email-verification.html", "Email successfully verified"),
    WELCOME_MESSAGE("welcome-message.html", "Welcome to the banking system"),
    PASSWORD_RESET("password-reset.html", "Password reset");

    @Getter
    private final String template;
    @Getter
    private final String subject;

    EmailTemplate(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}