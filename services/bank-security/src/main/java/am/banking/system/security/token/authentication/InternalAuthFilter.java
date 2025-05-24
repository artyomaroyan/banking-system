package am.banking.system.security.token.authentication;

import am.banking.system.common.tls.configuration.InternalSecretProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

import static am.banking.system.common.enums.PermissionEnum.GENERATE_SYSTEM_TOKEN;
import static am.banking.system.common.enums.RoleEnum.SYSTEM;

/**
 * Author: Artyom Aroyan
 * Date: 25.05.25
 * Time: 00:53:37
 */
@Component
@RequiredArgsConstructor
public class InternalAuthFilter implements WebFilter {
    private final InternalSecretProperties secretProperties;


    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String header = exchange.getRequest().getHeaders().getFirst("X-internal-Secret");
        if (header != null && header.equals(secretProperties.secret())) {
            var auth = new UsernamePasswordAuthenticationToken(
                    "internal-client",
                    null,
                    List.of(
                            new SimpleGrantedAuthority(SYSTEM.name()),
                            new SimpleGrantedAuthority(GENERATE_SYSTEM_TOKEN.name())
                    ));
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        }
        return chain.filter(exchange);
    }
}