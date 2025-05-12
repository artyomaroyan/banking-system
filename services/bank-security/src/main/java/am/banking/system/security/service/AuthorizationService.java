package am.banking.system.security.service;

import am.banking.system.common.enums.PermissionEnum;
import am.banking.system.security.token.validator.TokenClaimsExtractor;
import am.banking.system.security.token.validator.abstraction.IJwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 22.04.25
 * Time: 23:47:47
 */
@Component
@RequiredArgsConstructor
public class AuthorizationService {
    private final IJwtTokenValidator jwtTokenValidator;
    private final TokenClaimsExtractor tokenClaimsExtractor;

    public boolean isAuthorized(String token, PermissionEnum permission) {
        var username = jwtTokenValidator.extractUsername(token);
        if (!jwtTokenValidator.isValidToken(token, username)) {
            return false;
        }
        Set<PermissionEnum> permissions = tokenClaimsExtractor.extractPermissions(token);
        return permissions.contains(permission);
    }
}