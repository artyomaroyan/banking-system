package am.banking.system.common.ssl;

import am.banking.system.common.ssl.configuration.SecuritySSLProperties;
import am.banking.system.common.ssl.exception.WebClientSslContextException;
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

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 22:12:36
 */
@Slf4j
public class SslContextBuilder {

    public static SslContext buildSslContext(SecuritySSLProperties properties) {

        try {
            KeyStore keyStore = KeyStore.getInstance(properties.keyStoreType());
            try (InputStream ksStream = new FileInputStream(properties.keyStorePath())) {
                keyStore.load(ksStream, properties.keyStorePassword());
                log.info("Loaded keystore from {}", properties.keyStorePath());
            }

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, properties.keyStorePassword());

            KeyStore trustStore = KeyStore.getInstance(properties.trustStoreType());
            try (InputStream tsStream = new FileInputStream(properties.trustStorePath())) {
                trustStore.load(tsStream, properties.trustStorePassword());
                log.info("Loaded truststore from {}", properties.trustStorePath());
            }

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            return io.netty.handler.ssl.SslContextBuilder.forClient()
                    .keyManager(kmf)
                    .trustManager(tmf)
                    .build();
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException |
                 UnrecoverableKeyException ex) {
            throw new WebClientSslContextException("Failed to build SSL context", ex);
        }
    }
}