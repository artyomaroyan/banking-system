package am.banking.system.notification.domain.entity;

import am.banking.system.common.shared.model.BaseEntity;
import am.banking.system.notification.domain.enums.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 00:41:22
 */
@Getter
@Builder
@AllArgsConstructor
@Table("notification.notification")
public class Notification extends BaseEntity {
    @Column("email_type")
    private final EmailType emailType;
    @Column("notification_date")
    private final LocalDateTime notificationDate;
    @Column("recipient_email")
    private final String recipientEmail;
    @Column("username")
    private final String username;
    @Column("verification_link")
    private final String verificationLink;
}