package am.banking.system.user.infrastructure.configuration;

import am.banking.system.user.exception.WebClientSslContextException;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

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

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:27:13
 */
@Slf4j
@Configuration
public class WebClientConfiguration {
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean("webClientConfig")
    public WebClient securityWebClient(
            @Value("${security.service.url}") String url,
            @Value("${webclient.ssl.keyStorePath}") String keyStorePath,
            @Value("${webclient.ssl.keyStorePassword}") String keyStorePassword,
            @Value("${webclient.ssl.keyStoreType}") String keyStoreType,
            @Value("${webclient.ssl.trustStorePath}") String trustStorePath,
            @Value("${webclient.ssl.trustStorePassword}") String trustStorePassword,
            @Value("${webclient.ssl.trustStoreType}") String trustStoreType) {

        SslContext sslContext = buildNettySslContext(keyStorePath, keyStorePassword, keyStoreType,
                trustStorePath, trustStorePassword, trustStoreType);

        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .secure(t -> t.sslContext(sslContext))))
                .build();
    }

    private SslContext buildNettySslContext(
            String keyStorePath,
            String keyStorePassword,
            String keyStoreType,
            String trustStorePath,
            String trustStorePassword,
            String trustStoreType) {

        try {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            try (InputStream ksStream = new FileInputStream(keyStorePath)) {
                keyStore.load(ksStream, keyStorePassword.toCharArray());
                log.info("Loaded keystore from {}", keyStorePath);
            }

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keyStorePassword.toCharArray());

            KeyStore trustStore = KeyStore.getInstance(trustStoreType);
            try (InputStream tsStream = new FileInputStream(trustStorePath)) {
                trustStore.load(tsStream, trustStorePassword.toCharArray());
                log.info("Loaded truststore from {}", trustStorePath);
            }

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            return SslContextBuilder.forClient()
                    .keyManager(kmf)
                    .trustManager(tmf)
                    .build();
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException |
                 UnrecoverableKeyException ex) {
            throw new WebClientSslContextException("Failed to build SSL context", ex);
        }
    }
}