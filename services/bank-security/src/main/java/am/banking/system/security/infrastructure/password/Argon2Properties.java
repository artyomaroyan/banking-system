package am.banking.system.security.infrastructure.password;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
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
        @NotNull char[] salt,
        @NotNull char[] secretKey) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Argon2Properties(
                int memory1, int iterations1, int parallelism1, int length, char[] salt1, char[] key
        ))) return false;
        return memory == memory1 &&
                iterations == iterations1 &&
                parallelism == parallelism1 &&
                hashLength == length &&
                Arrays.equals(salt, salt1) &&
                Arrays.equals(secretKey, key);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(memory, iterations, parallelism, hashLength);
        result = 31 * result + Arrays.hashCode(salt);
        result = 31 * result + Arrays.hashCode(secretKey);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "Argon2 properties{" +
                "memory = " + memory +
                ", iterations = " + iterations +
                ", parallelism = " + parallelism +
                ", hashLength = " + hashLength +
                ", salt = [PROTECTED]" +
                ", secretKey = [PROTECTED]" +
                "}";
    }
}