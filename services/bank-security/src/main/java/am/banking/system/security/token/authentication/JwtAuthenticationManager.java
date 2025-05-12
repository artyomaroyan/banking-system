package am.banking.system.security.token.authentication;

import am.banking.system.security.token.validator.JwtTokenValidator;
import am.banking.system.security.token.validator.TokenClaimsExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 13.05.25
 * Time: 01:23:12
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtTokenValidator jwtTokenValidator;
    private final TokenClaimsExtractor tokenClaimsExtractor;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username = jwtTokenValidator.extractUsername(authToken);
        if (jwtTokenValidator.isValidToken(authToken, username)) {
            List<GrantedAuthority> authorities = tokenClaimsExtractor.extractAuthorities(authToken);
            return Mono.just(new UsernamePasswordAuthenticationToken(username, authToken, authorities));
        }
        return Mono.empty();
    }
}