package am.banking.system.security.application.port.in;

import jakarta.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 14.07.25
 * Time: 00:05:06
 */
public interface InvalidateTokenUseCase {
    Mono<Long> markTokenAsVerified(@NotBlank String token);
    Mono<Long> markTokensForciblyExpired();
}