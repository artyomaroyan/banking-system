package am.banking.system.security.api.controller;

import am.banking.system.security.application.token.strategy.KeyProviderStrategy;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.interfaces.ECPublicKey;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Author: Artyom Aroyan
 * Date: 18.05.25
 * Time: 21:34:22
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/.well-known")
public class JwtTokenDecoderController {
    private final KeyProviderStrategy keyProviderStrategy;

    @GetMapping(value = "/jwks.json", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> getJwkSet() {
        try {
            ECPublicKey ecPublicKey = (ECPublicKey) keyProviderStrategy.getPublicKey();
            ECKey ecKey = new ECKey.Builder(Curve.P_256, ecPublicKey)
                    .keyUse(KeyUse.SIGNATURE)
                    .algorithm(JWSAlgorithm.ES256)
                    .keyID(keyProviderStrategy.getKeyId())
                    .build();

            Map<String, Object> jwkSet = new JWKSet(ecKey).toJSONObject();
            log.info("JWKSet is {}", jwkSet);
            return Mono.just(ResponseEntity.ok(jwkSet));
        }  catch (Exception e) {
            log.error("Failed to get JWKSet", e);
            return Mono.just(ResponseEntity.status(INTERNAL_SERVER_ERROR).build());
        }
    }
}