package am.banking.system.security.infrastructure.token.filter;

import am.banking.system.security.application.port.in.JwtTokenValidatorUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Author: Artyom Aroyan
 * Date: 14.06.25
 * Time: 21:40:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InternalTokenAuthenticationFilter implements WebFilter {
    private final JwtTokenValidatorUseCase jwtTokenValidator;

    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/.well-known/jwks.json", // <- this was an issue which has tormented me 1 day, because I do not include it in exclude path list my decoder does not work and show me an empty page
            "/api/v1/secure/local/system-token"
    );

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (EXCLUDED_PATHS.contains(path)) {
            log.debug("Bypassing InternalTokenAuthenticationFilter for path: {}", path);
            return chain.filter(exchange);
        }

        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(AUTHORIZATION))
                .filter(header -> header.startsWith("Bearer "))
                .flatMap(header -> {
                    String token = header.substring(7);

                    return jwtTokenValidator.validateInternalToken(token)
                            .doOnNext(jwt -> log.info("Authenticating service [{}] with authorities {}",
                                    jwt.getSubject(),
                                    jwt.getClaim("authorities")))

                            .flatMap(_ -> chain.filter(exchange))
                            .onErrorResume(error -> {
                                log.error("Authentication failed for token {} with error: {}",
                                        token.substring(0, Math.min(8, token.length())),
                                        error.getMessage());
                                exchange.getResponse().setStatusCode(UNAUTHORIZED);
                                return exchange.getResponse().setComplete();
                            });
                });
    }
}