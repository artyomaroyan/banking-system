package am.banking.system.common.tls.configuration;

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
@ConfigurationProperties(prefix = "webclient.tls")
public record SecurityTLSProperties(
        @NotBlank String url,
        @NotBlank String keyStore,
        @NotNull char[] keyStorePassword,
        @NotBlank String keyStoreType,
        @NotBlank String trustStore,
        @NotNull char[] trustStorePassword,
        @NotBlank String trustStoreType) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SecurityTLSProperties that = (SecurityTLSProperties) obj;
        return Objects.equals(this.url, that.url) &&
                Objects.equals(this.keyStore, that.keyStore) &&
                Arrays.equals(this.keyStorePassword, that.keyStorePassword) &&
                Objects.equals(this.keyStoreType, that.keyStoreType) &&
                Objects.equals(this.trustStore, that.trustStore) &&
                Arrays.equals(this.trustStorePassword, that.trustStorePassword) &&
                Objects.equals(this.trustStoreType, that.trustStoreType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(url, keyStore, keyStoreType, trustStore, trustStoreType);
        result = 31 * result + Arrays.hashCode(keyStorePassword);
        result = 31 * result + Arrays.hashCode(trustStorePassword);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "SecurityTLSProperties{" +
                "url='" + url + '\'' +
                ", keyStore='" + keyStore + '\'' +
                ", keyStorePassword=[PROTECTED]" +
                ", keyStoreType='" + keyStoreType + '\'' +
                ", trustStore='" + trustStore + '\'' +
                ", trustStorePassword=[PROTECTED]" +
                ", trustStoreType='" + trustStoreType + '\'' +
                '}';
    }
}