package am.banking.system.notification.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static am.banking.system.common.infrastructure.tls.WebClientFilter.errorResponseFilter;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 02:53:01
 */
@Configuration
public class NotificationWebClientConfiguration {

    @Bean
    public WebClient notificationWebClient() {
        return WebClient.builder()
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .filter(errorResponseFilter())
                .build();
    }
}