package am.banking.system.security.token.service.abstraction;

import am.banking.system.security.model.dto.UserPrincipal;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:00:48
 */
public interface IJwtTokenService {
    String generateJwtToken(UserPrincipal user);
    String generateSystemToken();
}