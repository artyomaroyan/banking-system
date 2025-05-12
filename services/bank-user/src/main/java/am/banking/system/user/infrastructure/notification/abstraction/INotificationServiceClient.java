package am.banking.system.user.infrastructure.notification.abstraction;

import am.banking.system.common.dto.notification.EmailVerification;
import am.banking.system.common.dto.notification.PasswordReset;
import am.banking.system.common.dto.notification.WelcomeMessage;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 03:29:42
 */
public interface INotificationServiceClient {
    Void sendEmailVerificationEmail(EmailVerification email);
    Void sendPasswordResetEmail(PasswordReset email);
    Void sendWelcomeEmail(WelcomeMessage email);
}