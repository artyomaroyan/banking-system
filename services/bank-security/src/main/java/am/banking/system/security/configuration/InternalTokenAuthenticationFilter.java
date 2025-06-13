package am.banking.system.security.configuration;

import am.banking.system.common.tls.configuration.InternalSecretProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Author: Artyom Aroyan
 * Date: 05.06.25
 * Time: 01:31:18
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InternalTokenAuthenticationFilter implements WebFilter {
    private final InternalSecretProperties internalSecretProperties;

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (path.startsWith("/.well-known/jwks.json")) {
            return chain.filter(exchange);
        }

        log.info("Custom Log:: Intercepted request: {}", path);

        if ("/api/v1/secure/local/system-token".equals(path)) {
            List<String> tokenHeaders = request.getHeaders().get("X-Internal-Secret");

            log.info("Custom Log:: Header 'X-Internal-Secret': {}",  tokenHeaders);
            log.info("Custom Log:: Expected Secret: {}", internalSecretProperties.secret());

            if (tokenHeaders == null || tokenHeaders.isEmpty()) {
                log.warn("Custom Log:: Missing X-Internal-Secret header");
                return unauthorized("Missing Internal Secret");
            }

            String receivedSecret = tokenHeaders.getFirst();
            if (!internalSecretProperties.secret().equals(receivedSecret)) {
                log.warn("Custom Log:: Invalid secret received: {}", receivedSecret);
                return unauthorized("Invalid Internal Secret");
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> unauthorized(String message) {
        return Mono.error(new ResponseStatusException(UNAUTHORIZED, message));
    }
}