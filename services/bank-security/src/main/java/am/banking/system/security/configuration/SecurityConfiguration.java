package am.banking.system.security.configuration;

import am.banking.system.common.infrastructure.tls.configuration.InternalSecretProperties;
import am.banking.system.security.application.port.in.UserTokenValidatorUseCase;
import am.banking.system.security.converter.JwtReactiveAuthenticationConverter;
import am.banking.system.security.infrastructure.token.filter.InternalTokenAuthenticationFilter;
import am.banking.system.security.infrastructure.token.filter.InternalTokenSecretFilter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

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
    private final MeterRegistry meterRegistry;
    private final ServerWebExchangeMatcher csrfMatcher;
    private final UserTokenValidatorUseCase userTokenValidator;
    private final CorsConfigurationSource corsConfigurationSource;
    private final InternalSecretProperties internalSecretProperties;
    private final JwtReactiveAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    protected SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.requireCsrfProtectionMatcher(csrfMatcher))
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(PublicEndpoints.ALL)
                            .permitAll()
                        .pathMatchers("/api/internal/security/password/hash",
                                "/api/internal/security/user-token/email/issue")
                        .hasAnyAuthority("ROLE_SYSTEM", "DO_INTERNAL_TASKS")
                            .anyExchange()
                                .authenticated()
                )
                .addFilterBefore(new InternalTokenSecretFilter(meterRegistry, internalSecretProperties),
                        SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(new InternalTokenAuthenticationFilter(userTokenValidator),
                        SecurityWebFiltersOrder.AUTHENTICATION)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }
}