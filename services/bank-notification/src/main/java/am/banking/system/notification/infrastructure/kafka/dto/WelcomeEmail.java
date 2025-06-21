package am.banking.system.notification.infrastructure.kafka.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Artyom Aroyan
 * Date: 15.05.25
 * Time: 13:41:39
 */
public record WelcomeEmail(String email, String username) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}