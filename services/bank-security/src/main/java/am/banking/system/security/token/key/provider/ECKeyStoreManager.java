package am.banking.system.security.token.key.provider;

import am.banking.system.security.exception.InvalidECKeyType;
import am.banking.system.security.exception.KeyIdGenerationException;
import am.banking.system.security.exception.KeyStoreLoadException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.KeyUse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 00:45:40
 */
@Slf4j
@Service
public class ECKeyStoreManager {

    public KeyStore loadKeyStore(String path, char[] password) {
        File keystoreFile = new File(path);
        if (!keystoreFile.exists()) {
            throw new KeyStoreLoadException("Keystore file does not exist at:" + path);
        }

        try (FileInputStream stream = new FileInputStream(keystoreFile)) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(stream, password);
            return keyStore;

        } catch (Exception ex) {
            log.error("Failed to load keystore from: {}", path, ex);
            throw new KeyStoreLoadException("Failed to load keystore" + path, ex);
        }
    }

    public ECPrivateKey loadPrivateKey(KeyStore keyStore, String alias, char[] password) {
        try {
            Key key = keyStore.getKey(alias, password);
            if (!(key instanceof ECPrivateKey)) {
                throw new InvalidECKeyType("Private key is not an ECPrivateKey" + key.getClass().getName());
            }
            return (ECPrivateKey) key;

        } catch (Exception ex) {
            log.error("Failed to load private key with alias: {}", alias, ex);
            throw new KeyStoreLoadException("Failed to load private key: " + alias, ex);
        }
    }

    public ECPublicKey loadPublicKey(KeyStore keyStore, String alias) {
        try {
            Certificate certificate = keyStore.getCertificate(alias);
            PublicKey publicKey = certificate.getPublicKey();
            if (!(publicKey instanceof ECPublicKey)) {
                throw new InvalidECKeyType("Public key is not an ECPublicKey" + publicKey.getClass().getName());
            }
            return (ECPublicKey) publicKey;

        } catch (Exception ex) {
            log.error("Failed to load public key with alias: {}", alias, ex);
            throw new KeyStoreLoadException("Failed to load public key: " + alias, ex);
        }
    }

    public String generateKeyIdFromPublicKey(ECPublicKey publicKey) {
        try {
            ECKey ecKey = new ECKey.Builder(Curve.P_256, publicKey)
                    .keyUse(KeyUse.SIGNATURE)
                    .algorithm(JWSAlgorithm.ES256)
                    .keyIDFromThumbprint()
                    .build();
            return ecKey.getKeyID();

        } catch (JOSEException ex) {
            log.error("Failed to generate Key ID from public key", ex);
            throw new KeyIdGenerationException("Failed to generate Key ID from public key", ex);
        }
    }
}