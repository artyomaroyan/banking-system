package am.banking.system.security.token.configuration;

import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * Author: Artyom Aroyan
 * Date: 17.04.25
 * Time: 01:32:11
 */
@ConfigurationProperties(value = "spring.application.security.token.password")
public record UserTokenProperties(
        TokenSpec passwordRecovery,
        TokenSpec emailVerification
) {
    @Validated
    public record TokenSpec(
            @NotBlank String secret,
            @NotBlank String algorithm,
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