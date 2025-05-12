package am.banking.system.security.configuration;

import am.banking.system.security.token.authentication.JwtAuthenticationConverter;
import am.banking.system.security.token.authentication.JwtAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION;
import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

/**
 * Author: Artyom Aroyan
 * Date: 20.04.25
 * Time: 00:26:06
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
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
            "/api/v1/user/account/login/**",
            "/api/v1/user/account/verify-email/**",
            "/api/v1/user/password-reset/send-email",
            "/api/v1/user/password-reset/reset/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("authorization", "content-type", "bearer"));
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    protected SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        configureCsrf(http);
        configureCors(http);
        configureSessionManagement(http);
        configureAuthentication(http);
        configureJwtAuthentication(http);

        return http.build();
    }

    private void configureCsrf(ServerHttpSecurity http) {
        ServerWebExchangeMatcher csrfMatcher = exchange -> {
            for (String path : CSRF_IGNORE) {
                if (Objects.requireNonNull(pathMatchers(path).matches(exchange).block()).isMatch()) {
                    return ServerWebExchangeMatcher.MatchResult.notMatch();
                }
            }
            return ServerWebExchangeMatcher.MatchResult.match();
        };

        http.csrf(csrf -> csrf
                .requireCsrfProtectionMatcher(csrfMatcher)
        );
    }

    private void configureCors(ServerHttpSecurity http) {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
    }

    private void configureSessionManagement(ServerHttpSecurity http) {
        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
    }

    private void configureAuthentication(ServerHttpSecurity http) {
        http.authorizeExchange(exchange -> exchange
                .pathMatchers(PUBLIC_URLS).permitAll()
                .pathMatchers(
                        "/api/v1/user/account/register/**",
                        "/api/v1/user/account/login",
                        "/api/v1/user/account/verify-email/**",
                        "/api/v1/user/password-reset/send-email",
                        "/api/v1/user/password-reset/reset/**"
                ).permitAll()
                .anyExchange().authenticated()
        );
    }

    private void configureJwtAuthentication(ServerHttpSecurity http) {
        AuthenticationWebFilter jwtAuthenticationWebFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
        jwtAuthenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter);
        jwtAuthenticationWebFilter.setAuthenticationFailureHandler(
                (exchange, _) -> {
                    exchange.getExchange().getResponse().setStatusCode(UNAUTHORIZED);
                    return exchange.getExchange().getResponse().setComplete();
                }
        );
        http.addFilterAt(jwtAuthenticationWebFilter, AUTHENTICATION);
    }
}