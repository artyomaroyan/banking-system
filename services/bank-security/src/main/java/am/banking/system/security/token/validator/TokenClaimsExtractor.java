package am.banking.system.security.token.validator;

import am.banking.system.common.enums.PermissionEnum;
import am.banking.system.security.token.strategy.KeyProviderStrategy;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PublicKey;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:53:51
 */
@Component
@RequiredArgsConstructor
public class TokenClaimsExtractor {
    private final KeyProviderStrategy jwtTokenKeyProvider;

    public Claims extractAllClaims(final String token, final Key signingKey) {
        JwtParserBuilder parserBuilder = Jwts.parser();
        switch (signingKey) {
            case SecretKey secretKey -> parserBuilder.verifyWith(secretKey);
            case PublicKey publicKey -> parserBuilder.verifyWith(publicKey);
            default -> throw new IllegalArgumentException("Unsupported key type: " + signingKey.getClass());
        }

        return parserBuilder
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Set<PermissionEnum> extractPermissions(final String token) {
        var claims = extractAllClaims(token, jwtTokenKeyProvider.getPublicKey());
        var permissions = (List<?>) claims.get("permissions");
        if (permissions == null) return Set.of();
        return permissions.stream()
                .map(Object::toString)
                .map(PermissionEnum::valueOf)
                .collect(Collectors.toSet());
    }

    public List<GrantedAuthority> extractAuthorities(String token) {
        var claims = extractAllClaims(token, jwtTokenKeyProvider.getPublicKey());
        Object rolesClaim = claims.get("roles");
        if (!(rolesClaim instanceof List<?>)) {
            return Collections.emptyList();
        }
        return ((List<?>) rolesClaim).stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());
    }
}