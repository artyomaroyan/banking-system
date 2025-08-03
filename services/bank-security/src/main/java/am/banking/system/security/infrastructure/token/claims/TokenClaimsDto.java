package am.banking.system.security.infrastructure.token.claims;

import am.banking.system.common.shared.enums.AccountState;
import am.banking.system.security.domain.enums.TokenPurpose;
import am.banking.system.security.domain.enums.TokenState;
import lombok.Builder;

import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 17.04.25
 * Time: 01:36:08
 */
@Builder
public record TokenClaimsDto(Integer userId, String username, String email, Set<String> roles, Set<String> permissions,
                             TokenState tokenState, TokenPurpose tokenPurpose, AccountState accountState) {
}