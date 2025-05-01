package am.banking.system.common.ssl.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.Objects;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 15:50:24
 */
@Validated
@ConfigurationProperties(prefix = "webclient.ssl")
public record SecuritySSLProperties(
        @NotBlank String url,
        @NotBlank String keyStorePath,
        @NotNull char[] keyStorePassword,
        @NotBlank String keyStoreType,
        @NotBlank String trustStorePath,
        @NotNull char[] trustStorePassword,
        @NotBlank String trustStoreType) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SecuritySSLProperties that = (SecuritySSLProperties) obj;
        return Objects.equals(this.url, that.url) &&
                Objects.equals(this.keyStorePath, that.keyStorePath) &&
                Arrays.equals(this.keyStorePassword, that.keyStorePassword) &&
                Objects.equals(this.keyStoreType, that.keyStoreType) &&
                Objects.equals(this.trustStorePath, that.trustStorePath) &&
                Arrays.equals(this.trustStorePassword, that.trustStorePassword) &&
                Objects.equals(this.trustStoreType, that.trustStoreType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(url, keyStorePath, keyStoreType, trustStorePath, trustStoreType);
        result = 31 * result + Arrays.hashCode(keyStorePassword);
        result = 31 * result + Arrays.hashCode(trustStorePassword);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "SecuritySSLProperties{" +
                "url='" + url + '\'' +
                ", keyStorePath='" + keyStorePath + '\'' +
                ", keyStorePassword=[PROTECTED]" +
                ", keyStoreType='" + keyStoreType + '\'' +
                ", trustStorePath='" + trustStorePath + '\'' +
                ", trustStorePassword=[PROTECTED]" +
                ", trustStoreType='" + trustStoreType + '\'' +
                '}';
    }
}