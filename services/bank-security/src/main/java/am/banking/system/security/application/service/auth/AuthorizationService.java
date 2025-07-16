package am.banking.system.security.application.service.auth;

import am.banking.system.common.shared.enums.PermissionEnum;
import am.banking.system.security.application.port.in.AuthorizationUseCase;
import am.banking.system.security.application.port.in.UserTokenValidatorUseCase;
import am.banking.system.security.application.validator.TokenClaimsExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 22.04.25
 * Time: 23:47:47
 */
@Component
@RequiredArgsConstructor
public class AuthorizationService implements AuthorizationUseCase {
    private final TokenClaimsExtractor tokenClaimsExtractor;
    private final UserTokenValidatorUseCase userTokenValidator;

    @Override
    public Mono<Boolean> isAuthorized(String token, PermissionEnum permission) {
        return userTokenValidator.extractValidClaims(token)
                .flatMap(_ -> {
                    Set<PermissionEnum> permissions = tokenClaimsExtractor.extractPermissions(token);
                    return Mono.just(permissions.contains(permission));
                })
                .defaultIfEmpty(false);
    }
}