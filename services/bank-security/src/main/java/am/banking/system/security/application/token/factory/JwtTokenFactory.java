package am.banking.system.security.application.token.factory;

import am.banking.system.security.application.token.strategy.KeyProviderStrategy;
import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.infrastructure.token.key.TokenSigningKeyManager;
import am.banking.system.security.application.token.strategy.TokenGenerationStrategy;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:50:29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFactory implements TokenGenerationStrategy {
    private final KeyProviderStrategy keyProviderStrategy;
    private final TokenSigningKeyManager tokenSigningKeyManager;

    @Override
    public String generateToken(Map<String, Object> claims, String subject) {
        var type = TokenType.JSON_WEB_TOKEN;
        var credentials = tokenSigningKeyManager.getSigningCredentials(type);
        var issuedAt = new Date();
        var expiration = new Date(issuedAt.getTime() + tokenSigningKeyManager.getTokenExpiration(type));

        var builder = Jwts.builder()
                .header()
                .keyId(keyProviderStrategy.getKeyId())
                    .and()
                .claims(claims)
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .issuer("Token issuer")
                .audience().add("Bank account web client")
                    .and()
                .id(UUID.randomUUID().toString());
        return credentials.sign(builder).compact();
    }

    @Override
    public TokenType getSupportedTokenType() {
        return TokenType.JSON_WEB_TOKEN;
    }
}