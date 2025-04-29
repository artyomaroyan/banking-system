package am.banking.system.security.token.service;

import am.banking.system.security.model.dto.UserPrincipal;
import am.banking.system.security.model.enums.TokenType;
import am.banking.system.security.token.service.abstraction.IJwtTokenService;
import am.banking.system.security.token.service.abstraction.ITokenService;
import am.banking.system.security.token.claims.TokenClaimsMapper;
import am.banking.system.security.token.claims.TokenClaimsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:27:56
 */
@Service
@RequiredArgsConstructor
public class JwtTokenService implements IJwtTokenService {
    private final ITokenService tokenService;
    private final TokenClaimsMapper tokenClaimsMapper;
    private final TokenClaimsService tokenClaimsService;

    @Override
    public String generateJwtToken(final UserPrincipal principal) {
        var claimsDto = tokenClaimsService.createJwtTokenClaims(principal);
        var claims = tokenClaimsMapper.mapTokenClaims(claimsDto);
        return tokenService.createToken(claims, principal.getUsername(), TokenType.JSON_WEB_TOKEN);
    }
}