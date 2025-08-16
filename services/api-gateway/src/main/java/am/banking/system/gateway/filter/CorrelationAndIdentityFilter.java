package am.banking.system.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 16.08.25
 * Time: 21:05:31
 */
@Component
public class CorrelationAndIdentityFilter implements GlobalFilter, Ordered {
    private static final String HDR_CORRELATION_ID = "X-Correlation-Id";
    private static final String HDR_USER_ID = "X-User-Id";
    private static final String HDR_ROLES = "X-Roles";
    private static final String HDR_SCOPES = "X-Scopes";
    private static final String HDR_TOKEN_ID = "X-Token-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String correlationId = request.getHeaders().getFirst(HDR_CORRELATION_ID);
        if (correlationId == null ||  correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        ServerHttpRequest.Builder builder = request.mutate().header(HDR_CORRELATION_ID, correlationId);

        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.empty())
                .flatMap(ctx -> {
                    if (ctx != null && ctx.getAuthentication() != null &&
                    ctx.getAuthentication().getPrincipal() instanceof Jwt jwt) {
                        builder.header(HDR_USER_ID, jwt.getSubject() != null ? jwt.getSubject() : "");

                        Collection<? extends GrantedAuthority> authorities = ctx.getAuthentication().getAuthorities();
                        String roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
                        builder.header(HDR_ROLES, roles);

                        String scope = "";
                        if (jwt.getClaim("scope")) {
                            scope = String.join(",", jwt.getClaimAsString("scope"));
                        } else if (jwt.hasClaim("scp")) {
                            scope = String.join(",", jwt.getClaimAsString("scp"));
                        }
                        builder.header(HDR_SCOPES, scope);

                        builder.header(HDR_TOKEN_ID, jwt.getId() != null ? jwt.getId() : "");
                    }
                    return chain.filter(exchange.mutate().request(builder.build()).build());
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}