package am.banking.system.user.application.port.out;

import jakarta.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 06.07.25
 * Time: 15:30:47
 */
public interface TokenInvalidateClientPort {
    Mono<String> invalidateUsedToken(@NotBlank String token);
}