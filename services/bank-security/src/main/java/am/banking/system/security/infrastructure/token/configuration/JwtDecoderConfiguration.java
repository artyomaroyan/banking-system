package am.banking.system.security.infrastructure.token.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

/**
 * Author: Artyom Aroyan
 * Date: 17.05.25
 * Time: 18:55:26
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
class JwtDecoderConfiguration {
    private final Environment environment;
    @Bean
    protected ReactiveJwtDecoder jwtDecoder() {
        String port = environment.getProperty("server.port", "8989");
        String jwkSetUri = "http://localhost:" + port + "/.well-known/jwks.json";
        log.info("Configuring JWT decoder with JWKSetUri {}", jwkSetUri);

        return NimbusReactiveJwtDecoder
                .withJwkSetUri(jwkSetUri)
                .jwsAlgorithm(SignatureAlgorithm.ES256)
                .build();
    }
}