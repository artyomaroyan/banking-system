package am.banking.system.security.application.port.in;

import io.jsonwebtoken.Claims;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:20:19
 */
public interface UserTokenValidatorUseCase {
    Mono<Jwt> validateInternalToken(final String token);
    Mono<Claims> extractValidClaims(final String token);
    Mono<Boolean> isValidPasswordResetToken(final UUID userId, final String token);
    Mono<Boolean> isValidEmailVerificationToken(final UUID userId, final String token);
}