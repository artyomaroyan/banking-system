package am.banking.system.security.token.key.provider;

import am.banking.system.security.model.enums.TokenType;
import am.banking.system.security.token.dto.SigningCredentials;
import am.banking.system.security.token.strategy.SigningKeyProviderStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 00:59:46
 */
@Component
public class TokenSigningKeyManager {
    private final JwtSigningKeyProvider jwtSigningKeyProvider;
    private final SystemTokenKeyProvider systemTokenKeyProvider;
    private final PasswordRecoveryKeyProvider passwordRecoveryKeyProvider;
    private final EmailVerificationKeyProvider emailVerificationKeyProvider;
    private final Map<TokenType, SigningKeyProviderStrategy<? extends Key>> signingKeyProvider;

    @Autowired
    public TokenSigningKeyManager(JwtSigningKeyProvider jwtSigningKeyProvider, PasswordRecoveryKeyProvider passwordRecoveryKeyProvider, EmailVerificationKeyProvider emailVerificationKeyProvider, List<SigningKeyProviderStrategy<? extends Key>> signingKeyProviders, SystemTokenKeyProvider systemTokenKeyProvider) {
        this.jwtSigningKeyProvider = jwtSigningKeyProvider;
        this.systemTokenKeyProvider = systemTokenKeyProvider;
        this.passwordRecoveryKeyProvider = passwordRecoveryKeyProvider;
        this.emailVerificationKeyProvider = emailVerificationKeyProvider;
        this.signingKeyProvider = signingKeyProviders.stream()
                .collect(Collectors.toMap(SigningKeyProviderStrategy::getTokenType,
                        strategy -> strategy));
    }

    public SigningCredentials<? extends Key> getSigningCredentials(TokenType type) {
        SigningKeyProviderStrategy<? extends Key> strategy = signingKeyProvider.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No SigningKeyProviderStrategy found for type " + type);
        }
        return strategy.signingCredentials();
    }

    public Long getTokenExpiration(TokenType type) {
        return switch (type) {
            case JSON_WEB_TOKEN -> jwtSigningKeyProvider.getTokenExpiration();
            case PASSWORD_RESET -> passwordRecoveryKeyProvider.getTokenExpiration();
            case EMAIL_VERIFICATION -> emailVerificationKeyProvider.getTokenExpiration();
            case INTERNAL_JWT_TOKEN -> systemTokenKeyProvider.getTokenExpiration();
        };
    }
}