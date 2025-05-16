package am.banking.system.security.token.key.provider;

import am.banking.system.security.model.enums.TokenType;
import am.banking.system.security.token.strategy.KeyProviderStrategy;
import am.banking.system.security.token.strategy.SigningKeyProviderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;

/**
 * Author: Artyom Aroyan
 * Date: 17.05.25
 * Time: 00:50:31
 */
@Component
@RequiredArgsConstructor
public class SystemTokenKeyProvider implements SigningKeyProviderStrategy {
    private final KeyProviderStrategy keyProviderStrategy;

    @Override
    public Key getSigningKey() {
        return keyProviderStrategy.getPrivateKey();
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.INTERNAL_JWT_TOKEN;
    }

    @Override
    public Long getTokenExpiration() {
        return Duration.ofMinutes(5).toMillis();
    }
}