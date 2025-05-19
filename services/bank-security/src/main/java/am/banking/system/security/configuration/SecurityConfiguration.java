package am.banking.system.security.configuration;

import am.banking.system.security.certification.CertificateAuthenticationConverter;
import am.banking.system.security.certification.CertificateAuthenticationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static am.banking.system.common.enums.PermissionEnum.GENERATE_SYSTEM_TOKEN;

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
    private final CertificateAuthenticationManager certificateAuthenticationManager;
    private final CertificateAuthenticationConverter certificateAuthenticationConverter;

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
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    protected SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.requireCsrfProtectionMatcher(customCsrfMatcher()))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(PUBLIC_URLS)
                            .permitAll()
                        .pathMatchers(
                                "api/v1/user/account/register/**",
                                "api/v1/user/account/activate/**"
                        )
                            .permitAll()
                        .pathMatchers("/api/v1/secure/local/system-token")
                            .hasAuthority(GENERATE_SYSTEM_TOKEN.name())
                        .anyExchange()
                            .authenticated()
                )
                .authenticationManager(certificateAuthenticationManager)
                .addFilterAt(certificateAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    @Bean
    protected ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> authorities = jwt.getClaimAsStringList("authorities");
            if (authorities == null || authorities.isEmpty()) {
                return Flux.empty();
            }
            return Flux.fromIterable(authorities.stream()
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .toList());
        });
        return converter;
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:8080", "http://localhost:8888", "http://localhost:8761",
                "http://localhost:9090", "http://localhost:8989", "http://localhost:8040", "http://localhost:8090"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("authorization", "content-type", "x-auth-token", "bearer "));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private AuthenticationWebFilter certificateAuthenticationWebFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(certificateAuthenticationManager);
        filter.setServerAuthenticationConverter(certificateAuthenticationConverter);
        return filter;
    }

    private ServerWebExchangeMatcher customCsrfMatcher() {
        return exchange ->
                Mono.just(exchange.getRequest().getPath().value())
                        .flatMap(path -> {
                            for (String ignore : CSRF_IGNORE) {
                                if (path.matches(ignore.replace("**", ".*"))) {
                                    return ServerWebExchangeMatcher.MatchResult.notMatch();
                                }
                            }
                            return ServerWebExchangeMatcher.MatchResult.match();
                        });
    }
}