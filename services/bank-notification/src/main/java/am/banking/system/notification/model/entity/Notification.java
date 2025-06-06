package am.banking.system.notification.model.entity;

import am.banking.system.common.entity.BaseEntity;
import am.banking.system.notification.kafka.dto.EmailVerification;
import am.banking.system.notification.kafka.dto.PasswordReset;
import am.banking.system.notification.kafka.dto.WelcomeEmail;
import am.banking.system.notification.model.enums.EmailType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 00:41:22
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification", schema = "bank-notification")
public class Notification extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private EmailType emailType;
    private LocalDateTime notificationDate;
    private EmailVerification emailVerification;
    private PasswordReset passwordReset;
    private WelcomeEmail welcomeEmail;
}