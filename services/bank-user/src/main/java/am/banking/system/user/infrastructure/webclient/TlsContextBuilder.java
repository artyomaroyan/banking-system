package am.banking.system.user.infrastructure.webclient;

import am.banking.system.common.shared.exception.WebClientTLSContextException;
import io.netty.handler.ssl.SslContext;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static io.netty.handler.ssl.SslContextBuilder.*;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 22:12:36
 */
@Slf4j
public class TlsContextBuilder {

    // even if class name is SslContext by default it creates TLS connection.
    public static SslContext buildSslContext(TlsProperties properties) {
        try {
            KeyStore keyStore = KeyStore.getInstance(properties.keyStoreType());
            try (InputStream ksStream = new FileInputStream(properties.keyStore())) {
                keyStore.load(ksStream, properties.keyStorePassword());
                log.info("Loaded keystore from {}", properties.keyStore());
            }

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, properties.keyStorePassword());

            KeyStore trustStore = KeyStore.getInstance(properties.trustStoreType());
            try (InputStream tsStream = new FileInputStream(properties.trustStore())) {
                trustStore.load(tsStream, properties.trustStorePassword());
                log.info("Loaded truststore from {}", properties.trustStore());
            }

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            return forClient()
                    .keyManager(kmf)
                    .trustManager(tmf)
                    .protocols("TLSv1.3", "TLSv1.2")
                    .build();
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException |
                 UnrecoverableKeyException ex) {
            throw new WebClientTLSContextException("Failed to build SSL context", ex);
        }
    }
}