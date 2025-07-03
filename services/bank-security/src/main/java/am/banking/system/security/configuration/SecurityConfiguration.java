package am.banking.system.security.configuration;

import am.banking.system.common.infrastructure.tls.configuration.InternalSecretProperties;
import am.banking.system.security.application.port.in.JwtTokenValidatorUseCase;
import am.banking.system.security.infrastructure.token.filter.InternalTokenAuthenticationFilter;
import am.banking.system.security.infrastructure.token.filter.InternalTokenSecretFilter;
import io.micrometer.core.instrument.MeterRegistry;
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
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private final JwtTokenValidatorUseCase jwtTokenValidator;

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
            // internal security
            "/api/v1/secure/local/system-token",
            "/api/internal/security/system/token",
            "/api/internal/security/jwt/generate",
            "/api/internal/security/jwt/validate",
            "/api/internal/security/password/hash",
            "/api/internal/security/password/validate",
            "/api/internal/security/user-token/email/issue",
            "/api/internal/security/user-token/email/validate",
            "/api/internal/security/user-token/password-reset/issue",
            "/api/internal/security/user-token/password-reset/validate",
            "/api/internal/security/token/invalidate",
            "/api/internal/security/authorize",
            "/.well-known/jwks.json",
            // notification
            "/api/notification/email-verification",
            "/api/notification/password-reset",
            "/api/notification/welcome-email",
            // swagger
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    protected SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, MeterRegistry registry,
                                                            InternalSecretProperties properties) {
        http
                .csrf(csrf -> csrf.requireCsrfProtectionMatcher(customCsrfMatcher()))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(PUBLIC_URLS)
                            .permitAll()
                        .pathMatchers(
                                "/api/v1/user/account/register/**",
                                "/api/v1/user/account/activate/**",
                                "/api/internal/security/system/token",
                                "/api/v1/secure/local/system-token",
                                "/.well-known/jwks.json"
                        )
                            .permitAll()
                        .pathMatchers("/api/internal/security/hash-password",
                                "/api/internal/security/user-token/email/issue")
                                .hasAnyAuthority("ROLE_SYSTEM", "DO_INTERNAL_TASKS")
                        .anyExchange()
                            .authenticated()
                )
                .addFilterBefore(new InternalTokenSecretFilter(registry, properties), SecurityWebFiltersOrder.AUTHENTICATION)
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
                "http://localhost:8090", "/.well-known/jwks.json",
                // internal security
                "/api/v1/secure/local/system-token",
                "/api/internal/security/system/token",
                "/api/internal/security/jwt/generate",
                "/api/internal/security/jwt/validate",
                "/api/internal/security/password/hash",
                "/api/internal/security/password/validate",
                "/api/internal/security/user-token/email/issue",
                "/api/internal/security/user-token/email/validate",
                "/api/internal/security/user-token/password-reset/issue",
                "/api/internal/security/user-token/password-reset/validate",
                "/api/internal/security/token/invalidate",
                "/api/internal/security/authorize",
                // notification
                "/api/notification/email-verification",
                "/api/notification/password-reset", "/api/notification/welcome-email"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("authorization", "content-type", "x-auth-token", "Bearer", "X-Internal-Secret"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private ServerWebExchangeMatcher customCsrfMatcher() {
        List<ServerWebExchangeMatcher> matchers = Arrays.stream(CSRF_IGNORE)
                .map(PathPatternParserServerWebExchangeMatcher::new)
                .collect(Collectors.toList());

        OrServerWebExchangeMatcher customMatcher = new OrServerWebExchangeMatcher(matchers);

        return exchanger -> customMatcher.matches(exchanger)
                .flatMap(result -> result.isMatch() ?
                        ServerWebExchangeMatcher.MatchResult.notMatch() :
                        ServerWebExchangeMatcher.MatchResult.match());
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