package am.banking.system.security.infrastructure.token.key;

import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.api.shared.AsymmetricSigningCredentials;
import am.banking.system.security.api.shared.SigningCredentials;
import am.banking.system.security.application.token.strategy.KeyProviderStrategy;
import am.banking.system.security.application.token.strategy.SigningKeyProviderStrategy;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 00:53:07
 */
@Component
@RequiredArgsConstructor
public class JwtSigningKeyProvider implements SigningKeyProviderStrategy<PrivateKey> {
    private final KeyProviderStrategy keyProviderStrategy;

    @Override
    public SigningCredentials<PrivateKey> signingCredentials() {
        return new AsymmetricSigningCredentials(keyProviderStrategy.getPrivateKey(), Jwts.SIG.ES256);
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.JSON_WEB_TOKEN;
    }

    @Override
    public Long getTokenExpiration() {
        return keyProviderStrategy.getExpiration();
    }
}