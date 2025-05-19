package am.banking.system.security.controller;

import am.banking.system.security.token.strategy.KeyProviderStrategy;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.ECPublicKey;
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

    @GetMapping("/jwks.json")
    public Map<String, Object> getJwkSet() {
        ECPublicKey ecPublicKey = (ECPublicKey) keyProviderStrategy.getPublicKey();

        ECKey ecKey = new ECKey.Builder(Curve.P_256, ecPublicKey)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.ES256)
                .keyID(keyProviderStrategy.getKeyId())
                .build();
        log.info("Custom Log:: JWK Set: {}", ecKey.toJSONObject());
        return new JWKSet(ecKey).toJSONObject();
    }
}