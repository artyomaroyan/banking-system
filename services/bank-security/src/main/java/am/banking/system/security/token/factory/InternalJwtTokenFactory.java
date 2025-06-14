package am.banking.system.security.token.factory;

import am.banking.system.security.model.enums.TokenType;
import am.banking.system.security.token.key.provider.TokenSigningKeyManager;
import am.banking.system.security.token.strategy.TokenGenerationStrategy;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        var issuedAt = new Date();
        var expiration = new Date(issuedAt.getTime() + tokenSigningKeyManager.getTokenExpiration(type));

        var builder = Jwts.builder()
                .subject(JwtTokenFactory.class.getSimpleName())
                .issuer("bank-security service")
                .issuedAt(issuedAt)
                .expiration(expiration)
                .audience().add("Internal communication Token")
                .and()
                .claim("authorities", List.of("ROLE_SYSTEM", "DO_INTERNAL_TASKS"))
                .id(UUID.randomUUID().toString());
        log.info("Custom Log:: Generated system token from token factory class: {}", credentials.sign(builder).compact());
        log.info("Custom Log:: Expiration: {}", expiration);
        return credentials.sign(builder).compact();
    }

    @Override
    public TokenType getSupportedTokenType() {
        return TokenType.INTERNAL_JWT_TOKEN;
    }
}