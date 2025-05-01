package am.banking.system.security.token.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.Objects;

/**
 * Author: Artyom Aroyan
 * Date: 17.04.25
 * Time: 01:32:11
 */
@Validated
@ConfigurationProperties(prefix = "security.token.jwt.keystore")
public record JwtTokenProperties(
        @NotBlank String path,
        @NotBlank String alias,
        @NotNull char[] password,
        @Positive Long expiration) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof JwtTokenProperties(String path1, String alias1, char[] password1, Long expiration1))) return false;
        return Objects.equals(this.path, path1) &&
                Objects.equals(this.alias, alias1) &&
                Arrays.equals(this.password, password1) &&
                Objects.equals(this.expiration, expiration1);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(path, alias, expiration);
        result = 31 * result + Arrays.hashCode(password);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "JwtTokenProperties{" +
                "path='" + path + '\'' +
                ", alias='" + alias + '\'' +
                ", password=[PROTECTED]" +
                ", expiration=" + expiration +
                '}';
    }
}