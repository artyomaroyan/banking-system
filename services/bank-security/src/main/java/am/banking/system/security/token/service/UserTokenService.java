package am.banking.system.security.token.service;

import am.banking.system.security.exception.TokenNotFoundException;
import am.banking.system.security.model.dto.UserPrincipal;
import am.banking.system.security.model.entity.UserToken;
import am.banking.system.security.model.enums.TokenPurpose;
import am.banking.system.security.model.enums.TokenType;
import am.banking.system.security.model.repository.UserTokenRepository;
import am.banking.system.security.token.service.abstraction.ITokenService;
import am.banking.system.security.token.service.abstraction.IUserTokenService;
import am.banking.system.security.token.claims.TokenClaimsMapper;
import am.banking.system.security.token.claims.TokenClaimsService;
import am.banking.system.security.token.key.provider.TokenSigningKeyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static am.banking.system.security.model.enums.TokenPurpose.ACCOUNT_VERIFICATION;
import static am.banking.system.security.model.enums.TokenPurpose.PASSWORD_RECOVERY;
import static am.banking.system.security.model.enums.TokenState.PENDING;
import static am.banking.system.security.model.enums.TokenState.VERIFIED;
import static am.banking.system.security.model.enums.TokenType.EMAIL_VERIFICATION;
import static am.banking.system.security.model.enums.TokenType.PASSWORD_RESET;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:38:24
 */
@Service
@RequiredArgsConstructor
public class UserTokenService implements IUserTokenService {
    private final ITokenService tokenService;
    private final TokenClaimsMapper tokenClaimsMapper;
    private final TokenClaimsService tokenClaimsService;
    private final UserTokenRepository userTokenRepository;
    private final TokenSigningKeyManager tokenSigningKeyManager;

    @Override
    public String generatePasswordResetToken(final UserPrincipal principal) {
        return generateAndSaveToken(principal, PASSWORD_RECOVERY, PASSWORD_RESET);
    }

    @Override
    public String generateEmailVerificationToken(final UserPrincipal principal) {
        return generateAndSaveToken(principal, ACCOUNT_VERIFICATION, EMAIL_VERIFICATION);
    }

    @Transactional
    @Override
    public void markTokenAsVerified(final Long tokenId) {
        UserToken token = userTokenRepository.findById(tokenId)
                .orElseThrow(() -> new TokenNotFoundException("Token not found with id " + tokenId));
        token.setTokenState(VERIFIED);
        // The save call is optional here as @Transactional will flush changes automatically
        userTokenRepository.save(token);
    }

    private String generateAndSaveToken(final UserPrincipal principal, final TokenPurpose purpose, final TokenType type) {
        var token = generateToken(principal, purpose, type);
        var expiration = calculateExpirationDate(type);
        saveUserToken(principal, token, purpose, expiration);
        return token;
    }

    private String generateToken(final UserPrincipal principal, final TokenPurpose purpose, final TokenType type) {
        var claimsDto = tokenClaimsService.createUserTokenClaims(principal, purpose);
        var claims = tokenClaimsMapper.mapTokenClaims(claimsDto);
        return tokenService.createToken(claims, principal.getUsername(), type);
    }

    private void saveUserToken(final UserPrincipal principal, final String token, final TokenPurpose purpose, final Date expiration) {
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        userToken.setExpirationDate(expiration);
        userToken.setTokenPurpose(purpose);
        userToken.setTokenState(PENDING);
        userToken.setUserId(principal.getUserId());
        userTokenRepository.save(userToken);
    }

    private Date calculateExpirationDate(final TokenType type) {
        var issuedAt = new Date();
        return new Date(issuedAt.getTime() + tokenSigningKeyManager.retrieveTokenExpiration(type));
    }
}