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

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
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