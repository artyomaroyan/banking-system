package am.banking.system.security.application.port.in;

import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:17:52
 */
public interface JwtTokenValidatorUseCase {
    boolean isValidToken(String token);
    String extractUsername(final String token);
    Mono<Jwt> validateInternalToken(String token);
}