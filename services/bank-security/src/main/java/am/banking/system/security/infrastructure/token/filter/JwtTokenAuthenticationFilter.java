package am.banking.system.security.infrastructure.token.filter;

import am.banking.system.security.application.validator.TokenClaimsExtractor;
import am.banking.system.security.application.port.in.JwtTokenValidatorUseCase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Author: Artyom Aroyan
 * Date: 17.04.25
 * Time: 01:35:02
 */
@Service
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenValidatorUseCase jwtTokenValidator;
    private final TokenClaimsExtractor tokenClaimsExtractor;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            final String token = header.substring(7);
            final String username = jwtTokenValidator.extractUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null &&
                    jwtTokenValidator.isValidToken(token)) {
                    List<GrantedAuthority> authorities = tokenClaimsExtractor.extractAuthorities(token);
                    var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
        }
        filterChain.doFilter(request, response);
    }
}