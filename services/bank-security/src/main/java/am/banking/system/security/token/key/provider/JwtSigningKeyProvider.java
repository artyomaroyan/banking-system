package am.banking.system.security.token.key.provider;

import am.banking.system.security.model.enums.TokenType;
import am.banking.system.security.token.strategy.KeyProviderStrategy;
import am.banking.system.security.token.strategy.SigningKeyProviderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 00:53:07
 */
@Component
@RequiredArgsConstructor
public class JwtSigningKeyProvider implements SigningKeyProviderStrategy {
    private final KeyProviderStrategy keyProviderStrategy;

    @Override
    public Key getSigningKey() {
        return keyProviderStrategy.getPrivateKey();
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