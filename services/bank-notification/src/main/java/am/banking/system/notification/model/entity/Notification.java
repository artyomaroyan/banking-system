package am.banking.system.notification.model.entity;

import am.banking.system.common.entity.BaseEntity;
import am.banking.system.notification.kafka.dto.EmailVerification;
import am.banking.system.notification.kafka.dto.PasswordReset;
import am.banking.system.notification.kafka.dto.WelcomeEmail;
import am.banking.system.notification.model.enums.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 00:41:22
 */
@Table
@Getter
@Builder
@AllArgsConstructor
public class Notification extends BaseEntity {
    private final EmailType emailType;
    private final LocalDateTime notificationDate;
    private final EmailVerification emailVerification;
    private final PasswordReset passwordReset;
    private final WelcomeEmail welcomeEmail;
}