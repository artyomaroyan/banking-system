package am.banking.system.security.token.key.provider;

import am.banking.system.security.model.enums.TokenType;
import am.banking.system.security.token.configuration.UserTokenProperties;
import am.banking.system.security.token.strategy.SigningKeyProviderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 01:43:59
 */
@RequiredArgsConstructor
@Component("emailVerificationKeyProvider")
public class EmailVerificationKeyProvider implements SigningKeyProviderStrategy {
    private final UserTokenProperties userTokenProperties;

    @Override
    public Key getSigningKey() {
        return userTokenProperties.getEmailVerificationKey();
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.EMAIL_VERIFICATION;
    }

    @Override
    public Long getTokenExpiration() {
        return userTokenProperties.emailVerification().expiration() * 60 * 1000;
    }
}