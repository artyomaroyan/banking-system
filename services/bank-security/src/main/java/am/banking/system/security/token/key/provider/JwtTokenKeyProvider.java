package am.banking.system.security.token.key.provider;

import am.banking.system.security.token.configuration.JwtTokenProperties;
import am.banking.system.security.token.key.loader.ECKeyStoreManager;
import am.banking.system.security.token.strategy.KeyProviderStrategy;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 00:54:47
 */
@Component
public class JwtTokenKeyProvider implements KeyProviderStrategy {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final Long expiration;

    public JwtTokenKeyProvider(JwtTokenProperties properties, ECKeyStoreManager manager) {
        KeyStore keyStore = manager.loadKeyStore(properties.path(), properties.password());
        this.privateKey = manager.loadPrivateKey(keyStore, properties.alias(), properties.password());
        this.publicKey = manager.loadPublicKey(keyStore, properties.alias());
        this.expiration = properties.expiration();
    }

    @Override
    public Key getPrivateKey() {
        return privateKey;
    }

    @Override
    public Key getPublicKey() {
        return publicKey;
    }

    @Override
    public Long getExpiration() {
        return expiration * 60 * 1000;
    }
}