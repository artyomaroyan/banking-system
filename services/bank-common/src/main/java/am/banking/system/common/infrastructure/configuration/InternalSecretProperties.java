package am.banking.system.common.infrastructure.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 25.05.25
 * Time: 00:57:41
 */
@Validated
@ConfigurationProperties(prefix = "internal.system")
public record InternalSecretProperties(@NotBlank String secret) {
}