package am.banking.system.security.token.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Author: Artyom Aroyan
 * Date: 17.04.25
 * Time: 01:32:11
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtTokenProperties.class)
class JwtKeyStoreConfiguration {
    private final JwtTokenProperties jwtTokenProperties;

    @Bean
    protected KeyStore createKeyStore() throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream stream = new FileInputStream(jwtTokenProperties.path())) {
            keyStore.load(stream, jwtTokenProperties.password());
        }
        return keyStore;
    }
}