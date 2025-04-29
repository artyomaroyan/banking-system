package am.banking.system.security.token.key.loader;

import am.banking.system.security.exception.InvalidECKeyType;
import am.banking.system.security.exception.KeyStoreLoadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
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
        try (FileInputStream stream = new FileInputStream(path)) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(stream, password);
            return keyStore;
        } catch (Exception ex) {
            log.error("Failed to load keystore", ex);
            throw new KeyStoreLoadException("Failed to load keystore" + path, ex.getCause());
        }
    }

    public PrivateKey loadPrivateKey(KeyStore keyStore, String alias, char[] password) {
        try {
            Key key = keyStore.getKey(alias, password);
            if (!(key instanceof ECPrivateKey)) {
                throw new InvalidECKeyType("Private key is not an EC private key" + key.getClass().getName());
            }
            return (ECPrivateKey) key;
        } catch (Exception ex) {
            log.error("Failed to load private key", ex);
            throw new KeyStoreLoadException("Failed to load private key" + alias, ex.getCause());
        }
    }

    public PublicKey loadPublicKey(KeyStore keyStore, String alias) {
        try {
            Certificate certificate = keyStore.getCertificate(alias);
            PublicKey publicKey = certificate.getPublicKey();
            if (!(publicKey instanceof ECPublicKey)) {
                throw new InvalidECKeyType("Public key is not an EC public key" + publicKey.getClass().getName());
            }
            return publicKey;
        } catch (Exception ex) {
            log.error("Failed to load public key", ex);
            throw new KeyStoreLoadException("Failed to load public key" + alias, ex.getCause());
        }
    }
}