package am.banking.system.gateway.configuration;

import jakarta.ws.rs.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 14.08.25
 * Time: 01:59:46
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class GatewaySecurityConfiguration {
    @Value("${spring.security.oauth2.resource-server.jwt.jwk-set-uri}")
    private final ReactiveJwtDecoder reactiveJwtDecoder;

    @Bean
    protected SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/.well-known/jwks.json")
                            .permitAll()
                        .pathMatchers(HttpMethod.OPTIONS, "/**")
                            .permitAll()
                        .anyExchange()
                            .authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth.jwt(
                        jwt -> jwt.jwtDecoder(reactiveJwtDecoder())));
        return http.build();
    }

    @Bean
    protected ReactiveJwtDecoder reactiveJwtDecoder() {
        return reactiveJwtDecoder;
    }

    // if reactiveJwtDecoder() method does not work try this.
//    @Bean
//    protected ReactiveJwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resource-server.jwt.jwk-set-uri}") String jwkSetUri) {
//        return NimbusReactiveJwtDecoder
//                .withJwkSetUri(jwkSetUri)
//                .build();
//    }

    @Bean
    protected KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress() != null ?
                        exchange.getRequest().getRemoteAddress().getAddress().getHostAddress() :
                        "unknown"
        );
    }
}