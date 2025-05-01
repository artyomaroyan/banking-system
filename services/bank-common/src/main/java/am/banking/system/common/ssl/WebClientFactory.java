package am.banking.system.common.ssl;

import am.banking.system.common.ssl.configuration.SecuritySSLProperties;
import io.netty.handler.ssl.SslContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static am.banking.system.common.ssl.SslContextBuilder.buildSslContext;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 22:50:01
 */
@Component
@RequiredArgsConstructor
public class WebClientFactory {
    private final SecuritySSLProperties securitySSLProperties;

    public WebClient createSecuredWebClient() {
        SslContext sslContext = buildSslContext(securitySSLProperties);
        return WebClient.builder()
                .baseUrl(securitySSLProperties.url())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().secure(c -> c.sslContext(sslContext))))
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }
}