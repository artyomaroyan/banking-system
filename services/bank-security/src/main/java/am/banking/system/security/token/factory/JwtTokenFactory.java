package am.banking.system.security.token.factory;

import am.banking.system.security.model.enums.TokenType;
import am.banking.system.security.token.key.provider.TokenSigningKeyManager;
import am.banking.system.security.token.strategy.TokenGenerationStrategy;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:50:29
 */
@Component
@RequiredArgsConstructor
public class JwtTokenFactory implements TokenGenerationStrategy {
    private final TokenSigningKeyManager tokenSigningKeyManager;

    @Override
    public String generateToken(Map<String, Object> claims, String subject) {
        var type = TokenType.JSON_WEB_TOKEN;
        var signingKey = tokenSigningKeyManager.retrieveSigningKey(type);
        var issuedAt = new Date();
        var expiration = new Date(issuedAt.getTime() + tokenSigningKeyManager.retrieveTokenExpiration(type));

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .issuer("Token issuer")
                .audience().add("Bank account web client")
                .and()
                .id(UUID.randomUUID().toString())
                .signWith(signingKey)
                .compact();
    }

    @Override
    public String generateSystemToken() {
        var type = TokenType.INTERNAL_JWT_TOKEN;
        var signingKey = tokenSigningKeyManager.retrieveSigningKey(type);
        var issuedAt = new Date();
        var expiration = new Date(issuedAt.getTime() + tokenSigningKeyManager.retrieveTokenExpiration(type));

        return Jwts.builder()
                .subject(JwtTokenFactory.class.getSimpleName())
                .issuer("bank-security service")
                .issuedAt(issuedAt)
                .expiration(expiration)
                .audience().add("Internal communication Token")
                .and()
                .claim("roles", "ROLE_SYSTEM")
                .id(UUID.randomUUID().toString())
                .signWith(signingKey)
                .compact();
    }

    @Override
    public TokenType getSupportedTokenType() {
        return TokenType.JSON_WEB_TOKEN;
    }
}