package am.banking.system.security.token.validator;

import am.banking.system.common.enums.PermissionEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PublicKey;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:53:51
 */
@Component
public class ExtractTokenClaims {
    public Claims extractAllClaims(final String token, final Key signingKey) {
        JwtParserBuilder parserBuilder = Jwts.parser();
        if (signingKey instanceof SecretKey secretKey) {
            parserBuilder.verifyWith(secretKey);
        } else if (signingKey instanceof PublicKey publicKey) {
            parserBuilder.verifyWith(publicKey);
        } else {
            throw new IllegalArgumentException("Unsupported key type: " + signingKey.getClass());
        }

        return parserBuilder
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    protected Set<PermissionEnum> extractPermissions(final String token, final Key signingKey) {
        var claims = extractAllClaims(token, signingKey);
        var permissions = (List<?>) claims.get("permissions");
        if (permissions == null) return Set.of();
        return permissions.stream()
                .map(Object::toString)
                .map(PermissionEnum::valueOf)
                .collect(Collectors.toSet());
    }
}