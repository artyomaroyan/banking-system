package am.banking.system.notification.kafka.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Artyom Aroyan
 * Date: 15.05.25
 * Time: 13:38:42
 */
public record EmailVerification(String email, String username, String link) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}