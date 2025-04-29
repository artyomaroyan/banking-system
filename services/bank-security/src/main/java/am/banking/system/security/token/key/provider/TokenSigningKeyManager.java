package am.banking.system.security.token.key.provider;

import am.banking.system.security.exception.NotFoundTokenTypeException;
import am.banking.system.security.model.enums.TokenType;
import am.banking.system.security.token.strategy.SigningKeyProviderStrategy;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TokenSigningKeyManager {
    private final JwtSigningKeyProvider jwtSigningKeyProvider;
    private final PasswordRecoveryKeyProvider passwordRecoveryKeyProvider;
    private final EmailVerificationKeyProvider emailVerificationKeyProvider;
    private final Map<TokenType, SigningKeyProviderStrategy> signingKeyProvider;

    @Autowired
    public TokenSigningKeyManager(JwtSigningKeyProvider jwtSigningKeyProvider, PasswordRecoveryKeyProvider passwordRecoveryKeyProvider, EmailVerificationKeyProvider emailVerificationKeyProvider, List<SigningKeyProviderStrategy> signingKeyProviders) {
        this.jwtSigningKeyProvider = jwtSigningKeyProvider;
        this.passwordRecoveryKeyProvider = passwordRecoveryKeyProvider;
        this.emailVerificationKeyProvider = emailVerificationKeyProvider;
        this.signingKeyProvider = signingKeyProviders.stream()
                .collect(Collectors.toMap(SigningKeyProviderStrategy::getTokenType,
                        strategy -> strategy));
    }

    public Key retrieveSigningKey(TokenType type) {
        final SigningKeyProviderStrategy strategy = signingKeyProvider.get(type);
        if (strategy == null) {
            throw new NotFoundTokenTypeException("No signing key found for type " + type);
        }
        return strategy.getSigningKey();
    }

    public Long retrieveTokenExpiration(TokenType type) {
        return switch (type) {
            case JSON_WEB_TOKEN -> jwtSigningKeyProvider.getTokenExpiration();
            case PASSWORD_RESET -> passwordRecoveryKeyProvider.getTokenExpiration();
            case EMAIL_VERIFICATION -> emailVerificationKeyProvider.getTokenExpiration();
        };
    }
}