package am.banking.system.security.api.controller;

import am.banking.system.security.api.shared.JwkSetResponse;
import am.banking.system.security.application.token.strategy.KeyProviderStrategy;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.interfaces.ECPublicKey;
import java.util.List;
import java.util.Map;

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

    @PostConstruct
    public void init() {
        log.info("JwtTokenDecoderController is initialized");
    }


    @GetMapping(value = "/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<JwkSetResponse>> getJwkSet() {
        ECPublicKey ecPublicKey = (ECPublicKey) keyProviderStrategy.getPublicKey();

        ECKey ecKey = new ECKey.Builder(Curve.P_256, ecPublicKey)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.ES256)
                .keyID(keyProviderStrategy.getKeyId())
                .build();

        Map<String, Object> jwkJson = new JWKSet(ecKey).toJSONObject();
        log.info("Custom Log:: JWK Set: {}", jwkJson);

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new JwkSetResponse(List.of(jwkJson))));
    }
}