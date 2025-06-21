package am.banking.system.security.configuration;

import am.banking.system.security.infrastructure.token.filter.InternalTokenAuthenticationFilter;
import am.banking.system.security.application.validator.IJwtTokenValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 20.04.25
 * Time: 00:26:06
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    private final IJwtTokenValidator jwtTokenValidator;

    private static final String[] PUBLIC_URLS = {
            "/webjars/**",
            "/v2/api-docs",
            "/v3/api-docs/",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/configuration/ui",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/security"
    };

    private static final String[] CSRF_IGNORE = {
            "/api/v1/user/account/register/**",
            "/api/v1/user/account/activate/**",
            "/api/v1/secure/local/system-token",
            "/api/security/web/hash-password",
            "/.well-known/jwks.json",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    protected SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.requireCsrfProtectionMatcher(customCsrfMatcher()))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(PUBLIC_URLS)
                            .permitAll()
                        .pathMatchers(
                                "/api/v1/user/account/register/**",
                                "/api/v1/user/account/activate/**",
                                "/api/v1/secure/local/system-token",
                                "/.well-known/jwks.json"
                        )
                            .permitAll()
                        .pathMatchers("/api/security/web/hash-password")
                                .hasAnyAuthority("ROLE_SYSTEM", "DO_INTERNAL_TASKS")
                        .anyExchange()
                            .authenticated()
                )
                .addFilterAt(new InternalTokenAuthenticationFilter(jwtTokenValidator), SecurityWebFiltersOrder.AUTHENTICATION)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:8080", "http://localhost:8888", "http://localhost:8761",
                "http://localhost:9090", "http://localhost:8989", "http://localhost:8040",
                "http://localhost:8090", "/.well-known/jwks.json", "/api/security/web/hash-password"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("authorization", "content-type", "x-auth-token", "Bearer", "X-Internal-Secret"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private ServerWebExchangeMatcher customCsrfMatcher() {
        return exchange ->
                Mono.just(exchange.getRequest().getPath().value())
                        .flatMap(path -> {
                            for (String ignore : CSRF_IGNORE) {
                                if (path.matches(ignore.replace("**", ""))) {
                                    return ServerWebExchangeMatcher.MatchResult.notMatch();
                                }
                            }
                            return ServerWebExchangeMatcher.MatchResult.match();
                        });
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter  jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        jwtAuthenticationConverter.setPrincipalClaimName("sub");
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}