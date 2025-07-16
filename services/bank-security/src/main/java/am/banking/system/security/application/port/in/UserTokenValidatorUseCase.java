package am.banking.system.security.application.port.in;

import io.jsonwebtoken.Claims;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:20:19
 */
public interface UserTokenValidatorUseCase {
    Mono<Jwt> validateInternalToken(String token);
    Mono<Claims> extractValidClaims(String token);
    Mono<Boolean> isValidPasswordResetToken(final String token);
    Mono<Boolean> isValidEmailVerificationToken(final String token);
}