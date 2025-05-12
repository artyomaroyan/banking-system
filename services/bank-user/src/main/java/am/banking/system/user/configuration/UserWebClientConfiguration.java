package am.banking.system.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 12:31:52
 */
@Configuration
public class UserWebClientConfiguration {

    @Bean
    public WebClient notificationWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8040")
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter(notificationExchangeFilter())
                .build();
    }

    private ExchangeFilterFunction notificationExchangeFilter() {
        return (request, next) -> next.exchange(request);
    }
}