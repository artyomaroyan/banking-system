package am.banking.system.security.application.service.auth;

import am.banking.system.common.shared.enums.PermissionEnum;
import am.banking.system.security.application.port.in.AuthorizationServiceUseCase;
import am.banking.system.security.application.validator.TokenClaimsExtractor;
import am.banking.system.security.application.port.in.JwtTokenValidatorUseCase;
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
public class AuthorizationService implements AuthorizationServiceUseCase {
    private final JwtTokenValidatorUseCase jwtTokenValidator;
    private final TokenClaimsExtractor tokenClaimsExtractor;

    @Override
    public boolean isAuthorized(String token, PermissionEnum permission) {
        var username = jwtTokenValidator.extractUsername(token);
        if (!jwtTokenValidator.isValidToken(token)) {
            return false;
        }
        Set<PermissionEnum> permissions = tokenClaimsExtractor.extractPermissions(token);
        return permissions.contains(permission);
    }
}