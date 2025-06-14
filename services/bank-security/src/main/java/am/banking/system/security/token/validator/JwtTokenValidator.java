package am.banking.system.security.token.validator;

import am.banking.system.security.token.strategy.KeyProviderStrategy;
import am.banking.system.security.token.validator.abstraction.IJwtTokenValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:54:43
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenValidator implements IJwtTokenValidator {
    private final ReactiveJwtDecoder jwtDecoder;
    private final KeyProviderStrategy jwtTokenKeyProvider;
    private final TokenClaimsExtractor tokenClaimsExtractor;

    @Override
    public boolean isValidToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername != null && extractedUsername.equals(username) && !isTokenExpired(token);
    }

    @Override
    public String extractUsername(final String token) {
        return tokenClaimsExtractor.extractAllClaims(token, jwtTokenKeyProvider.getPublicKey()).getSubject();
    }

    @Override
    public Mono<Jwt> validateInternalToken(String token) {
        return Mono.just(token)
                .flatMap(t -> jwtDecoder.decode(t)
                        .doOnNext(jwt -> {
                            log.info("Internal token successfully decoded: {}", jwt);
                            log.info("Token claims: {}", jwt.getClaims());
                            log.info("Token headers: {}", jwt.getHeaders());
                        }))
                .doOnSuccess(jwt -> log.debug("Valid JWT for subject {}", jwt.getSubject()))
                .doOnError(error -> log.error("JWT validation failed",  error))
                .onErrorMap(error -> new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_token", error.getMessage(), null),
                        "JWT validation failed"));
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(final String token) {
        return tokenClaimsExtractor.extractAllClaims(token, jwtTokenKeyProvider.getPublicKey()).getExpiration();
    }
}