package am.banking.system.user.infrastructure.webclient;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 20.07.25
 * Time: 23:12:51
 */
@Validated
@ConfigurationProperties(prefix = "webclient.services")
public record BaseUrlProperties(
        @NotBlank String accountBaseUrl,
        @NotBlank String securityBaseUrl,
        @NotBlank String notificationBaseUrl) {
}