package am.banking.system.notification.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 02:58:29
 */
@ConfigurationProperties(prefix = "notification.tls.base-url")
public record NotificationProperties(String baseUrl) {
}