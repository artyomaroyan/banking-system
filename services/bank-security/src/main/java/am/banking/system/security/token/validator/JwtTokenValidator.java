package am.banking.system.security.token.validator;

import am.banking.system.common.enums.PermissionEnum;
import am.banking.system.security.token.key.provider.JwtTokenKeyProvider;
import am.banking.system.security.token.validator.abstraction.IJwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:54:43
 */
@Component
@RequiredArgsConstructor
public class JwtTokenValidator implements IJwtTokenValidator {
    private final ExtractTokenClaims extractTokenClaims;
    private final JwtTokenKeyProvider jwtTokenKeyProvider;

    @Override
    public boolean isValidToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername != null && extractedUsername.equals(username) && !isTokenExpired(token);
    }

    @Override
    public String extractUsername(final String token) {
        return extractTokenClaims.extractAllClaims(token, jwtTokenKeyProvider.getPublicKey()).getSubject();
    }

    @Override
    public Set<PermissionEnum> extractPermissions(final String token) {
        return extractTokenClaims.extractPermissions(token, jwtTokenKeyProvider.getPublicKey());
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(final String token) {
        return extractTokenClaims.extractAllClaims(token, jwtTokenKeyProvider.getPublicKey()).getExpiration();
    }
}