package am.banking.system.security.token.key.provider;

import am.banking.system.security.token.configuration.JwtTokenProperties;
import am.banking.system.security.token.strategy.KeyProviderStrategy;

import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 00:54:47
 */
public class JwtTokenKeyProvider implements KeyProviderStrategy {
    private final ECPrivateKey privateKey;
    private final ECPublicKey publicKey;
    private final String keyId;
    private final Long expiration;

    private JwtTokenKeyProvider(ECPrivateKey privateKey, ECPublicKey publicKey, String keyId, Long expiration) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.keyId = keyId;
        this.expiration = expiration;
    }

    public static JwtTokenKeyProvider fromProperties(JwtTokenProperties properties, ECKeyStoreManager keyManager) {
        KeyStore keyStore = keyManager.loadKeyStore(properties.path(), properties.password());
        ECPrivateKey privateKey = keyManager.loadPrivateKey(keyStore, properties.alias(), properties.password());
        ECPublicKey publicKey = keyManager.loadPublicKey(keyStore, properties.alias());
        String keyId = keyManager.generateKeyIdFromPublicKey(publicKey);
        long expiration = properties.expiration() * 60 * 1000L;
        return new JwtTokenKeyProvider(privateKey, publicKey, keyId, expiration);
    }

    @Override
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    @Override
    public Key getPublicKey() {
        return publicKey;
    }

    @Override
    public String getKeyId() {
        return keyId;
    }

    @Override
    public Long getExpiration() {
        return expiration * 60 * 1000;
    }
}