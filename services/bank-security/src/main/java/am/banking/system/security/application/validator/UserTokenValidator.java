package am.banking.system.security.application.validator;

import am.banking.system.security.application.port.in.UserTokenValidatorUseCase;
import am.banking.system.security.domain.enums.TokenPurpose;
import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.domain.repository.UserTokenRepository;
import am.banking.system.security.infrastructure.token.key.TokenSigningKeyManager;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;

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
    private final TokenClaimsExtractor tokenClaimsExtractor;
    private final UserTokenRepository userTokenRepository;
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
                    if (!userToken.getTokenPurpose().equals(purpose)) {
                        log.error(INVALID_TOKEN_PURPOSE);
                        return Mono.just(false);
                    }

                    if (!userToken.getTokenState().equals(PENDING)) {
                        log.error(INVALID_TOKEN_STATE);
                        return Mono.just(false);
                    }

                    if (userToken.getExpirationDate().before(new Date())) {
                        log.error(EXPIRED_TOKEN);
                        return Mono.just(false);
                    }

                    try {
                        final Key key = tokenSigningKeyManager.getSigningCredentials(type).key();
                        tokenClaimsExtractor.extractAllClaims(token, key);
                        log.info(TOKEN_VALIDATION_SUCCESS);
                        return Mono.just(true);
                    } catch (SecurityException | MalformedJwtException ex) {
                        log.error(INVALID_TOKEN_SIGNATURE, ex.getMessage(), ex);
                    } catch (ExpiredJwtException ex) {
                        log.error(EXPIRED_TOKEN_MSG, ex.getMessage(), ex);
                    } catch (UnsupportedJwtException ex) {
                        log.error(UNSUPPORTED_TOKEN, ex.getMessage(), ex);
                    } catch (IllegalArgumentException ex) {
                        log.error(VALIDATION_FAILED, ex.getMessage(), ex);
                    }
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.fromRunnable(() -> log.error(TOKEN_NOT_FOUND))
                        .thenReturn(false));
    }
}