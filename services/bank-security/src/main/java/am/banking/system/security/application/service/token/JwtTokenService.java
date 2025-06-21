package am.banking.system.security.application.service.token;

import am.banking.system.security.api.shared.UserPrincipal;
import am.banking.system.security.application.port.in.JwtTokenServiceUseCase;
import am.banking.system.security.application.port.in.TokenServiceUseCase;
import am.banking.system.security.infrastructure.token.claims.TokenClaimsMapper;
import am.banking.system.security.infrastructure.token.claims.TokenClaimsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static am.banking.system.security.domain.enums.TokenType.INTERNAL_JWT_TOKEN;
import static am.banking.system.security.domain.enums.TokenType.JSON_WEB_TOKEN;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:27:56
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService implements JwtTokenServiceUseCase {
    private final TokenServiceUseCase tokenService;
    private final TokenClaimsMapper tokenClaimsMapper;
    private final TokenClaimsService tokenClaimsService;

    @Override
    public String generateJwtToken(final UserPrincipal principal) {
        var claimsDto = tokenClaimsService.createJwtTokenClaims(principal);
        var claims = tokenClaimsMapper.mapTokenClaims(claimsDto);
        return tokenService.createToken(claims, principal.getUsername(), JSON_WEB_TOKEN);
    }

    @Override
    public String generateSystemToken() {
        return tokenService.createSystemToken(INTERNAL_JWT_TOKEN);
    }
}