package am.banking.system.security.token.claims;

import am.banking.system.security.model.dto.UserPrincipal;
import am.banking.system.security.model.enums.TokenPurpose;
import am.banking.system.security.model.enums.TokenState;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 17.04.25
 * Time: 01:37:51
 */
@Service
public class TokenClaimsService {

    public TokenClaimsDto createUserTokenClaims(UserPrincipal principal, TokenPurpose purpose) {
        return TokenClaimsDto.builder()
                .userId(principal.getUserId())
                .username(principal.getUsername())
                .email(principal.getEmail())
                .roles(principal.getRoles().stream().map(Objects::toString).collect(Collectors.toSet()))
                .permissions(principal.getPermissions().stream().map(Object::toString).collect(Collectors.toSet()))
                .tokenState(TokenState.PENDING)
                .tokenPurpose(purpose)
                .accountState(principal.getAccountState())
                .build();
    }

    public TokenClaimsDto createJwtTokenClaims(UserPrincipal principal) {
        return TokenClaimsDto.builder()
                .userId(principal.getUserId())
                .username(principal.getUsername())
                .email(principal.getEmail())
                .roles(principal.getRoles().stream().map(Objects::toString).collect(Collectors.toSet()))
                .permissions(principal.getPermissions().stream().map(Object::toString).collect(Collectors.toSet()))
                .tokenState(TokenState.PENDING)
                .build();
    }
}