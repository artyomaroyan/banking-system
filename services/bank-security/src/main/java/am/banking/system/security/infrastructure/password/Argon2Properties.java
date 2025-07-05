package am.banking.system.security.infrastructure.password;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Author: Artyom Aroyan
 * Date: 16.04.25
 * Time: 23:31:36
 */
@Validated
@ConfigurationProperties("spring.application.argon2")
public record Argon2Properties(
        @Positive int memory,
        @Positive int iterations,
        @Positive int parallelism,
        @Positive int hashLength,
        @Positive int saltLength,
        @NotNull char[] pepper
) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        // Recommended for Java 16+ records
        // Uses pattern matching with instanceof (available since Java 16)
        // Safe, clean, and avoids unnecessary casting
        if (!(obj instanceof Argon2Properties other)) return false;
        // Compare record fields directly (records are immutable, safe for value comparison)
        return memory == other.memory() &&
                iterations == other.iterations() &&
                parallelism == other.parallelism() &&
                hashLength == other.hashLength() &&
                saltLength == other.saltLength();
    }

    @Override
    public int hashCode() {
        return Objects.hash(memory, iterations, parallelism, hashLength, saltLength);
    }

    @NonNull
    @Override
    public String toString() {
        return "Argon2 properties{" +
                "memory = " + memory +
                ", iterations = " + iterations +
                ", parallelism = " + parallelism +
                ", hashLength = " + hashLength +
                ", saltLength = " +  saltLength +
                ", pepper = [PROTECTED]" +
                "}";
    }
}