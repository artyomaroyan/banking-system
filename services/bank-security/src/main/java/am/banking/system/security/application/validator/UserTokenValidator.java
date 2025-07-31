package am.banking.system.security.application.validator;

import am.banking.system.security.application.port.in.UserTokenValidatorUseCase;
import am.banking.system.security.application.token.strategy.KeyProviderStrategy;
import am.banking.system.security.domain.enums.TokenPurpose;
import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.domain.repository.UserTokenRepository;
import am.banking.system.security.infrastructure.token.key.TokenSigningKeyManager;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import static am.banking.system.security.domain.enums.TokenPurpose.ACCOUNT_VERIFICATION;
import static am.banking.system.security.domain.enums.TokenPurpose.PASSWORD_RECOVERY;
import static am.banking.system.security.domain.enums.TokenState.PENDING;
import static am.banking.system.security.domain.enums.TokenType.EMAIL_VERIFICATION;
import static am.banking.system.security.domain.enums.TokenType.PASSWORD_RESET;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:57:05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserTokenValidator implements UserTokenValidatorUseCase {
    private final ReactiveJwtDecoder readJwtDecoder;
    private final UserTokenRepository userTokenRepository;
    private final KeyProviderStrategy keyProviderStrategy;
    private final TokenClaimsExtractor tokenClaimsExtractor;
    private final TokenSigningKeyManager tokenSigningKeyManager;

    @Override
    public Mono<Claims> extractValidClaims(String token) {
        return Mono.fromCallable(() -> {
            Claims claims = tokenClaimsExtractor.extractAllClaims(token, keyProviderStrategy.getPublicKey());
            String username = claims.getSubject();
            Date expiration =  claims.getExpiration();

            if (username != null && !username.trim().isBlank() && expiration.after(new Date())) {
                return claims;
            }
            throw new JwtException("Invalid token claims: either expired or empty username.");
        })
                .onErrorResume(error -> {
                    log.error("Claim extraction failed: {}", error.getMessage(), error);
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Jwt> validateInternalToken(String token) {
        log.info("Validating token: {}", token);
        return readJwtDecoder.decode(token)
                .doOnNext(jwt -> {
                    log.info("Internal token successfully decoded: {}", jwt);
                    log.info("Token claims: {}", jwt.getClaims());
                    log.info("Token headers: {}", jwt.getHeaders());
                })
                .doOnError(error -> {
                    log.error("Detailed validation error: {}", error.getMessage(), error);
                    if (error instanceof JwtException jwtException) {
                        log.error("JWT error details: {}", jwtException.getMessage());
                    }
                })
                .switchIfEmpty(Mono.error(new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_token", "Decoded JWT is empty", null),
                        "JWT validation failed"
                )))
                .doOnSuccess(jwt -> {
                    if (jwt != null) {
                        log.debug("Valid JWT for subject: {}", jwt.getSubject());
                    } else {
                        log.warn("JWT decode returned null");
                    }
                })
                .doOnError(error -> log.error("JWT validation failed", error))
                .onErrorMap(error -> new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_token", error.getMessage(), null), "JWT validation failed"
                ));
    }

    @Override
    public Mono<Boolean> isValidPasswordResetToken(Integer userId, String token) {
        return validateToken(userId, token, PASSWORD_RECOVERY, PASSWORD_RESET);
    }

    @Override
    public Mono<Boolean> isValidEmailVerificationToken(Integer userId, String token) {
        return validateToken(userId, token, ACCOUNT_VERIFICATION, EMAIL_VERIFICATION);
    }

    private Mono<Boolean> validateToken(final Integer userId, final String token, final TokenPurpose purpose, final TokenType type) {
        return userTokenRepository.findByToken(token)
                .filter(t -> Objects.equals(t.getUserId(), userId))
                .filter(t -> !t.getExpirationDate().isBefore(LocalDateTime.now()) &&
                        t.getTokenState().equals(PENDING) &&
                        t.getTokenPurpose().equals(purpose))
                .flatMap(_ -> {
                    try {
                        Key key = tokenSigningKeyManager.getSigningCredentials(type).key();
                        tokenClaimsExtractor.extractAllClaims(token, key);
                        return Mono.just(true);
                    } catch (JwtException _) {
                        return  Mono.just(false);
                    }
                })
                .defaultIfEmpty(false);
    }
}