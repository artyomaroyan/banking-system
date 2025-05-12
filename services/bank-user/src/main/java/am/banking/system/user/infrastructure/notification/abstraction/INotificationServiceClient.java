package am.banking.system.user.infrastructure.notification.abstraction;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 03:29:42
 */
public interface INotificationServiceClient {
    void sendVerificationEmail(String email, String username, String link);
    void sendPasswordResetEmail(String email, String username, String link);
    void sendWelcomeEmail(String email);
}