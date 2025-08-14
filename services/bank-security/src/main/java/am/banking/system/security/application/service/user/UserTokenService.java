package am.banking.system.security.application.service.user;

import am.banking.system.security.api.shared.UserPrincipal;
import am.banking.system.security.application.port.in.TokenGenerationUseCase;
import am.banking.system.security.application.port.in.UserTokenUseCase;
import am.banking.system.security.domain.enums.TokenPurpose;
import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.domain.model.UserToken;
import am.banking.system.security.domain.repository.UserTokenRepository;
import am.banking.system.security.infrastructure.token.claims.TokenClaimsMapper;
import am.banking.system.security.infrastructure.token.claims.TokenClaimsService;
import am.banking.system.security.infrastructure.token.key.TokenSigningKeyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static am.banking.system.security.domain.enums.TokenPurpose.*;
import static am.banking.system.security.domain.enums.TokenState.PENDING;
import static am.banking.system.security.domain.enums.TokenType.*;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:38:24
 */
@Service
@RequiredArgsConstructor
public class UserTokenService implements UserTokenUseCase {
    private final TokenClaimsMapper tokenClaimsMapper;
    private final TokenClaimsService tokenClaimsService;
    private final UserTokenRepository userTokenRepository;
    private final TokenGenerationUseCase tokenGenerationService;
    private final TokenSigningKeyManager tokenSigningKeyManager;

    @Override
    public Mono<String> generateSystemToken() {
        return Mono.fromSupplier(() -> tokenGenerationService.generateInternalToken(INTERNAL_JWT_TOKEN));
    }

    @Override
    public Mono<String> generateJwtAccessToken(UserPrincipal principal) {
        // todo: change JWT access token state to VALID when it is new generated and expired when schedule task checks it is expired.
        return generateAndSaveToken(principal, JWT_ACCESS_TOKEN, JSON_WEB_TOKEN);
    }

    @Override
    public Mono<String> generatePasswordResetToken(UserPrincipal principal) {
        return generateAndSaveToken(principal, PASSWORD_RECOVERY, PASSWORD_RESET);
    }

    @Override
    public Mono<String> generateEmailVerificationToken(UserPrincipal principal) {
        return generateAndSaveToken(principal, ACCOUNT_VERIFICATION, EMAIL_VERIFICATION);
    }

    private Mono<String> generateAndSaveToken(final UserPrincipal principal, final TokenPurpose purpose, final TokenType type) {
        var token = generateToken(principal, purpose, type);
        var expiration = calculateExpirationDate(type);
        return saveUserToken(principal, token, purpose, expiration).thenReturn(token);
    }

    private String generateToken(final UserPrincipal principal, final TokenPurpose purpose, final TokenType type) {
        var claimsDto = tokenClaimsService.createUserTokenClaims(principal, purpose);
        var claims = tokenClaimsMapper.mapTokenClaims(claimsDto);
        return tokenGenerationService.generateUserToken(claims, principal.getUsername(), type);
    }

    private Mono<Void> saveUserToken(final UserPrincipal principal, final String token, final TokenPurpose purpose, final Date exp) {
        LocalDateTime expiration = exp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        UserToken userToken = UserToken.builder()
                .userId(principal.userId())
                .token(token)
                .expirationDate(expiration)
                .tokenPurpose(purpose)
                .tokenState(PENDING)
                .build();
        return userTokenRepository.save(userToken).then();
    }

    private Date calculateExpirationDate(final TokenType type) {
        var issuedAt = new Date();
        return new Date(issuedAt.getTime() + tokenSigningKeyManager.getTokenExpiration(type));
    }
}