package am.banking.system.common.tls;

import am.banking.system.common.tls.configuration.SecurityTLSProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static am.banking.system.common.tls.TLSContextBuilder.buildSslContext;
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
    private final SecurityTLSProperties securityTLSProperties;

    public WebClient createSecuredWebClient(String baseUrl) {
        SslContext sslContext = buildSslContext(securityTLSProperties);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofSeconds(5))
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                        .secure(c -> c.sslContext(sslContext))))
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }
}