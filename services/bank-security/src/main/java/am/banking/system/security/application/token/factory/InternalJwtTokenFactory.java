package am.banking.system.security.application.token.factory;

import am.banking.system.security.application.token.strategy.TokenGenerationStrategy;
import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.infrastructure.token.key.TokenSigningKeyManager;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 05.06.25
 * Time: 02:35:31
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InternalJwtTokenFactory implements TokenGenerationStrategy {
    private final TokenSigningKeyManager tokenSigningKeyManager;

    @Override
    public String generateSystemToken() {
        var type = TokenType.INTERNAL_JWT_TOKEN;
        var credentials = tokenSigningKeyManager.getSigningCredentials(type);

        var now = Instant.now();
        var expiration = now.plusMillis(tokenSigningKeyManager.getTokenExpiration(type));

        var builder = Jwts.builder()
                .subject("Internal System Token")
                .issuer("bank-security service")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .audience().add("internal-communication")
                .and()
                .claim("authorities", List.of("ROLE_SYSTEM", "DO_INTERNAL_TASKS"))
                .id(UUID.randomUUID().toString());
        return credentials.sign(builder).compact();
    }

    @Override
    public TokenType getSupportedTokenType() {
        return TokenType.INTERNAL_JWT_TOKEN;
    }
}