package am.banking.system.security.application.service.user;

import am.banking.system.security.api.shared.UserPrincipal;
import am.banking.system.security.application.port.in.TokenGenerationUseCase;
import am.banking.system.security.application.port.in.UserTokenServiceUseCase;
import am.banking.system.security.domain.model.UserToken;
import am.banking.system.security.domain.enums.TokenPurpose;
import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.domain.repository.UserTokenRepository;
import am.banking.system.security.infrastructure.token.claims.TokenClaimsMapper;
import am.banking.system.security.infrastructure.token.claims.TokenClaimsService;
import am.banking.system.security.infrastructure.token.key.TokenSigningKeyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

import static am.banking.system.security.domain.enums.TokenPurpose.ACCOUNT_VERIFICATION;
import static am.banking.system.security.domain.enums.TokenPurpose.PASSWORD_RECOVERY;
import static am.banking.system.security.domain.enums.TokenState.PENDING;
import static am.banking.system.security.domain.enums.TokenType.EMAIL_VERIFICATION;
import static am.banking.system.security.domain.enums.TokenType.PASSWORD_RESET;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:38:24
 */
@Service
@RequiredArgsConstructor
public class UserTokenService implements UserTokenServiceUseCase {
    private final TokenGenerationUseCase tokenService;
    private final TokenClaimsMapper tokenClaimsMapper;
    private final TokenClaimsService tokenClaimsService;
    private final UserTokenRepository userTokenRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final TokenSigningKeyManager tokenSigningKeyManager;

    @Override
    public Mono<String> generatePasswordResetToken(final UserPrincipal principal) {
        return generateAndSaveToken(principal, PASSWORD_RECOVERY, PASSWORD_RESET);
    }

    @Override
    public Mono<String> generateEmailVerificationToken(final UserPrincipal principal) {
        return generateAndSaveToken(principal, ACCOUNT_VERIFICATION, EMAIL_VERIFICATION);
    }

    public Mono<Long> markTokensForciblyExpired() {
        String sql = """
                UPDATE security.user_token
                SET token_state = 'FORCIBLY_EXPIRED'
                WHERE token_state = 'PENDING' AND expires_at < CURRENT_TIMESTAMP
                """;

        return r2dbcEntityTemplate
                .getDatabaseClient()
                .sql(sql)
                .fetch()
                .rowsUpdated();
    }

    private Mono<String> generateAndSaveToken(final UserPrincipal principal, final TokenPurpose purpose, final TokenType type) {
        var token = generateToken(principal, purpose, type);
        var expiration = calculateExpirationDate(type);
        return saveUserToken(principal, token, purpose, expiration).thenReturn(token);
    }

    private String generateToken(final UserPrincipal principal, final TokenPurpose purpose, final TokenType type) {
        var claimsDto = tokenClaimsService.createUserTokenClaims(principal, purpose);
        var claims = tokenClaimsMapper.mapTokenClaims(claimsDto);
        return tokenService.createToken(claims, principal.getUsername(), type);
    }

    private Mono<Void> saveUserToken(final UserPrincipal principal, final String token, final TokenPurpose purpose, final Date expiration) {
        UserToken userToken = UserToken.builder()
                .token(token)
                .expirationDate(expiration)
                .tokenPurpose(purpose)
                .tokenState(PENDING)
                .userId(principal.getUserId())
                .build();
        return userTokenRepository.save(userToken).then();
    }

    private Date calculateExpirationDate(final TokenType type) {
        var issuedAt = new Date();
        return new Date(issuedAt.getTime() + tokenSigningKeyManager.getTokenExpiration(type));
    }
}