package am.banking.system.security.application.validator;

import am.banking.system.security.application.port.in.UserTokenValidatorUseCase;
import am.banking.system.security.domain.model.UserToken;
import am.banking.system.security.domain.enums.TokenPurpose;
import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.domain.repository.UserTokenRepository;
import am.banking.system.security.infrastructure.token.key.TokenSigningKeyManager;
import am.banking.system.security.util.LogConstants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

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
    private final TokenClaimsExtractor tokenClaimsExtractor;
    private final UserTokenRepository userTokenRepository;
    private final TokenSigningKeyManager tokenSigningKeyManager;

    @Override
    public boolean isValidEmailVerificationToken(final String token) {
        return validateToken(token, ACCOUNT_VERIFICATION, EMAIL_VERIFICATION);
    }

    @Override
    public boolean isValidPasswordResetToken(final String token) {
        return validateToken(token, PASSWORD_RECOVERY, PASSWORD_RESET);
    }

    @Override
    public String extractUsername(final String token, final TokenType type) {
        final Key key = tokenSigningKeyManager.getSigningCredentials(type).key();
        return tokenClaimsExtractor.extractAllClaims(token, key).getSubject();
    }

    private boolean validateToken(final String token, final TokenPurpose purpose, final TokenType type) {
        Optional<UserToken> userTokenOptional = userTokenRepository.findByToken(token);
        if (userTokenOptional.isEmpty()) {
            log.error(LogConstants.TOKEN_NOT_FOUND);
            return false;
        }

        UserToken userToken = userTokenOptional.get();
        if (!userToken.getTokenPurpose().equals(purpose)) {
            log.error(LogConstants.INVALID_TOKEN_PURPOSE);
            return false;
        }

        if (!userToken.getTokenState().equals(PENDING)) {
            log.error(LogConstants.INVALID_TOKEN_STATE);
            return false;
        }

        if (userToken.getExpirationDate().before(new Date())) {
            log.error(LogConstants.EXPIRED_TOKEN);
            return false;
        }

        try {
            final Key key = tokenSigningKeyManager.getSigningCredentials(type).key();
            tokenClaimsExtractor.extractAllClaims(token, key);
            log.info(LogConstants.TOKEN_VALIDATION_SUCCESS);
            return true;
        } catch (SecurityException | MalformedJwtException ex) {
            log.error(LogConstants.INVALID_TOKEN_SIGNATURE, ex);
        } catch (ExpiredJwtException ex) {
            log.error(LogConstants.EXPIRED_TOKEN, ex);
        } catch (UnsupportedJwtException ex) {
            log.error(LogConstants.UNSUPPORTED_TOKEN, ex);
        } catch (IllegalArgumentException ex) {
            log.error(LogConstants.VALIDATION_FAILED, ex);
        }
        return false;
    }
}