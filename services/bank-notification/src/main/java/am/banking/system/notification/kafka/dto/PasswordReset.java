package am.banking.system.notification.kafka.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Artyom Aroyan
 * Date: 15.05.25
 * Time: 13:40:45
 */
public record PasswordReset(String email, String username, String link) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}