package am.banking.system.security.infrastructure.token.configuration;

import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * Author: Artyom Aroyan
 * Date: 17.04.25
 * Time: 01:32:11
 */
@ConfigurationProperties(prefix = "spring.application.token.access")
public record UserTokenProperties(
         @NestedConfigurationProperty @NonNull TokenSpec passwordRecovery,
         @NestedConfigurationProperty @NonNull TokenSpec emailVerification
) {
    @Validated
    public record TokenSpec(
             @NotBlank String secret,
             @Positive Long expiration) {
    }

    public SecretKey getEmailVerificationKey() {
        return getKey(emailVerification().secret);
    }

    public SecretKey getPasswordRecoveryKey() {
        return getKey(passwordRecovery().secret);
    }

    private SecretKey getKey(final String secret) {
        byte[] secretBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }
}