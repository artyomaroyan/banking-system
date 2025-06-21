package am.banking.system.security.application.token.factory;

import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.infrastructure.token.key.TokenSigningKeyManager;
import am.banking.system.security.application.token.strategy.TokenGenerationStrategy;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:48:26
 */
@Component
@RequiredArgsConstructor
public class PasswordRecoveryTokenFactory implements TokenGenerationStrategy {
    private final TokenSigningKeyManager tokenSigningKeyManager;

    @Override
    public String generateToken(Map<String, Object> claims, String subject) {
        // todo: add more claims
        var type = TokenType.PASSWORD_RESET;
        var credentials = tokenSigningKeyManager.getSigningCredentials(type);
        var issuedAt = new Date();
        var expiresAt = new Date(issuedAt.getTime() + tokenSigningKeyManager.getTokenExpiration(type));

        var builder = Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .issuer("Token issuer")
                .audience().add("Bank account web client")
                .and()
                .id(UUID.randomUUID().toString());
        return credentials.sign(builder).compact();
    }

    @Override
    public TokenType getSupportedTokenType() {
        return TokenType.PASSWORD_RESET;
    }
}