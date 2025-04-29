package am.banking.system.security.token.claims;

import am.banking.system.security.model.enums.AccountState;
import am.banking.system.security.model.enums.TokenPurpose;
import am.banking.system.security.model.enums.TokenState;
import lombok.Builder;

import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 17.04.25
 * Time: 01:36:08
 */
@Builder
public record TokenClaimsDto(Long userId, String username, String email, Set<String> roles, Set<String> permissions,
                             TokenState tokenState, TokenPurpose tokenPurpose, AccountState accountState) {
}