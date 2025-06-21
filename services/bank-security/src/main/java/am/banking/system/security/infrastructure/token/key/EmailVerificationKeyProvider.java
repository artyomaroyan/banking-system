package am.banking.system.security.infrastructure.token.key;

import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.infrastructure.token.configuration.UserTokenProperties;
import am.banking.system.security.api.shared.HmacSigningCredentials;
import am.banking.system.security.api.shared.SigningCredentials;
import am.banking.system.security.application.token.strategy.SigningKeyProviderStrategy;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 01:43:59
 */
@RequiredArgsConstructor
@Component("emailVerificationKeyProvider")
public class EmailVerificationKeyProvider implements SigningKeyProviderStrategy<SecretKey> {
    private final UserTokenProperties userTokenProperties;

    @Override
    public SigningCredentials<SecretKey> signingCredentials() {
        return new HmacSigningCredentials(userTokenProperties.getEmailVerificationKey(), Jwts.SIG.HS256);
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