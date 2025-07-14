package am.banking.system.security.application.validator;

import am.banking.system.security.application.port.in.UserTokenValidatorUseCase;
import am.banking.system.security.domain.enums.TokenPurpose;
import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.domain.repository.UserTokenRepository;
import am.banking.system.security.infrastructure.token.key.TokenSigningKeyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.time.LocalDateTime;

import static am.banking.system.security.domain.enums.TokenPurpose.ACCOUNT_VERIFICATION;
import static am.banking.system.security.domain.enums.TokenPurpose.PASSWORD_RECOVERY;
import static am.banking.system.security.domain.enums.TokenState.PENDING;
import static am.banking.system.security.domain.enums.TokenType.EMAIL_VERIFICATION;
import static am.banking.system.security.domain.enums.TokenType.PASSWORD_RESET;
import static am.banking.system.security.util.LogConstants.*;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:57:05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserTokenValidator implements UserTokenValidatorUseCase {
    private final UserTokenRepository userTokenRepository;
    private final TokenClaimsExtractor tokenClaimsExtractor;
    private final TokenSigningKeyManager tokenSigningKeyManager;

    @Override
    public Mono<Boolean> isValidEmailVerificationToken(final String token) {
        return validateToken(token, ACCOUNT_VERIFICATION, EMAIL_VERIFICATION);
    }

    @Override
    public Mono<Boolean> isValidPasswordResetToken(final String token) {
        return validateToken(token, PASSWORD_RECOVERY, PASSWORD_RESET);
    }

    private Mono<Boolean> validateToken(final String token, final TokenPurpose purpose, final TokenType type) {
        return userTokenRepository.findByToken(token)
                .flatMap(userToken -> {
                    if (!purpose.equals(userToken.getTokenPurpose())) {
                        log.error("{} - expected: {}, actual: {}", INVALID_TOKEN_PURPOSE, purpose, userToken.getTokenPurpose());
                        return Mono.just(false);
                    }

                    if (!PENDING.equals(userToken.getTokenState())) {
                        log.error("{} - actual: {}", INVALID_TOKEN_STATE, userToken.getTokenState());
                        return Mono.just(false);
                    }

                    if (userToken.getExpirationDate().isBefore(LocalDateTime.now())) {
                        log.error(EXPIRED_TOKEN);
                        return Mono.just(false);
                    }

                    try {
                        final Key key = tokenSigningKeyManager.getSigningCredentials(type).key();
                        tokenClaimsExtractor.extractAllClaims(token, key);
                        log.info(TOKEN_VALIDATION_SUCCESS);
                        return Mono.just(true);
                    } catch (JwtException ex) {
                        log.error("Jwt validation failed: {} - {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
                        return Mono.just(false);
                    } catch (IllegalArgumentException ex) {
                        log.error(VALIDATION_FAILED, ex.getMessage(), ex);
                        return Mono.just(false);
                    }
                })
                .switchIfEmpty(Mono.fromRunnable(() -> log.error(TOKEN_NOT_FOUND))
                        .thenReturn(false));
    }
}