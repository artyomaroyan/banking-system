package am.banking.system.security.application.validator;

import am.banking.system.security.application.port.in.JwtTokenValidatorUseCase;
import am.banking.system.security.application.token.strategy.KeyProviderStrategy;
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
public class JwtTokenValidator implements JwtTokenValidatorUseCase {
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
        return jwtDecoder.decode(token)
                .doOnNext(jwt -> {
                    log.info("Internal token successfully decoded: {}", jwt);
                    log.info("Token claims: {}", jwt.getClaims());
                    log.info("Token headers: {}", jwt.getHeaders());
                })
                .switchIfEmpty(Mono.error(new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_token", "Decoded JWT is empty", null),
                        "JWT validation failed"
                )))
                .doOnSuccess(jwt -> {
                    if (jwt != null) {
                        log.debug("Valid JWT for subject: {}", jwt.getSubject());
                    } else  {
                        log.warn("JWT decode returned null");
                    }
                })
                .doOnError(error -> log.error("JWT validation failed", error))
                .onErrorMap(error -> new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_token", error.getMessage(), null), "JWT validation failed"
                ));
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(final String token) {
        return tokenClaimsExtractor.extractAllClaims(token, jwtTokenKeyProvider.getPublicKey()).getExpiration();
    }
}