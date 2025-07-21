package am.banking.system.security.infrastructure.token.filter;

import am.banking.system.common.infrastructure.configuration.InternalSecretProperties;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Author: Artyom Aroyan
 * Date: 05.06.25
 * Time: 01:31:18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InternalTokenSecretFilter implements WebFilter {
    private final MeterRegistry meterRegistry;
    private final InternalSecretProperties internalSecretProperties;

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/.well-known/jwks.json",
            "/actuator/health",
            "/v3/api-docs",
            "/swagger-ui.html"
    );

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        String method = request.getMethod().toString();

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        log.debug("InternalTokenSecretFilter active on path: {}, method: {}", path, method);
        meterRegistry.counter("authentication.requests", "path", path).increment();

        if ("/api/v1/secure/local/system-token".equalsIgnoreCase(path) &&
                HttpMethod.GET.matches(method)) {
            return validateInternalSecret(exchange, chain);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> validateInternalSecret(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.fromCallable(() -> exchange.getRequest().getHeaders().getFirst("X-Internal-Secret"))
                .flatMap(receivedSecret -> {
                    if (receivedSecret == null || receivedSecret.trim().isBlank()) {
                        log.warn("Custom Log:: Missing X-Internal-Secret header from {}", getClientIP(exchange));
                        meterRegistry.counter("authentication.failure", "reason", "missing_header").increment();
                        return unauthorized("Missing Internal Secret");
                    }

                    if (!internalSecretProperties.secret().equals(receivedSecret)) {
                        log.warn("Custom Log:: Invalid secret received from {}", getClientIP(exchange));
                        meterRegistry.counter("authentication.failure", "reason", "invalid_secret").increment();
                        return unauthorized("Invalid Internal Secret");
                    }

                    log.debug("Custom Log:: Successful internal authentication from {}", getClientIP(exchange));
                    return chain.filter(exchange);
                })
                .onErrorResume(er -> {
                    log.error("Custom Log:: Error during internal authentication: {}", er.getMessage());
                    return unauthorized("Internal secret authentication failed");
                });
    }

    private String getClientIP(ServerWebExchange exchange) {
        return Optional.of(exchange.getRequest())
                .map(ServerHttpRequest::getRemoteAddress)
                .map(InetSocketAddress::getAddress)
                .map(InetAddress::getHostAddress)
                .orElse("unknown");
    }

    private Mono<Void> unauthorized(String message) {
        return Mono.defer(() -> {
            meterRegistry.counter("authentication.responses", "status", "401").increment();
            return Mono.error(new ResponseStatusException(
                    UNAUTHORIZED,
                    message,
                    new AuthenticationException(message)
            ));
        });
    }

    private boolean isPublicPath(String path) {
        return  PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}