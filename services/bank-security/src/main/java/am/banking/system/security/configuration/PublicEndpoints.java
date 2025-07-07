package am.banking.system.security.configuration;

import java.util.stream.Stream;

/**
 * Author: Artyom Aroyan
 * Date: 04.07.25
 * Time: 14:29:54
 */
final class PublicEndpoints {
    static final String[] SWAGGER = {
            "/webjars/**", "/v2/api-docs", "/v3/api-docs/", "/v3/api-docs/**",
            "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources", "/swagger-resources/**",
            "/configuration/ui", "/configuration/security"
    };

    static final String[] ACCOUNT = {
            "/api/v1/user/account/register/**",
            "/api/v1/user/account/activate",
            "/api/v1/secure/local/system-token",
            "/api/internal/security/system/token"
    };

    static final String[] JWKS = {
            "/.well-known/jwks.json"
    };

    static final String[] ALL = Stream.of(SWAGGER, ACCOUNT, JWKS)
            .flatMap(Stream::of)
            .toArray(String[]::new);
}