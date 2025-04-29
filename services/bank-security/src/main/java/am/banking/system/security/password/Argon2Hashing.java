package am.banking.system.security.password;

import am.banking.system.security.exception.InvalidEncodedPasswordFormatException;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * Author: Artyom Aroyan
 * Date: 16.04.25
 * Time: 23:30:23
 */
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(Argon2Properties.class)
public class Argon2Hashing implements PasswordEncoder {

    private final Argon2Properties argon2Properties;

    /**
     * Encodes the raw password using the Argon2 hashing algorithm.
     *
     * @param rawPassword the raw password to encode
     * @return the encoded password in a Base64 format: salt:secret:hashedPassword
     */
    @Override
    public String encode(CharSequence rawPassword) {
        byte[] salt = getSalt();
        byte[] secret = getSecretKey();
        byte[] password = hashPassword(rawPassword.toString(), salt, secret);

        Base64.Encoder encoder = Base64.getEncoder();
        return String.join(":",
                encoder.encodeToString(salt),
                encoder.encodeToString(secret),
                encoder.encodeToString(password));
    }

    /**
     * Verifies whether the raw password matches the encoded password.
     *
     * @param rawPassword     the raw password to verify
     * @param encodedPassword the previously encoded password to match against
     * @return true if the raw password matches the encoded password, false otherwise
     * @throws InvalidEncodedPasswordFormatException if the encoded password format is invalid
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String[] parts = encodedPassword.split(":");
        if (parts.length != 3) {
            throw new InvalidEncodedPasswordFormatException("Invalid encoded password format");
        }
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] salt = decoder.decode(parts[0]);
        byte[] secret = decoder.decode(parts[1]);
        byte[] expected = decoder.decode(parts[2]);
        byte[] actual = hashPassword(rawPassword.toString(), salt, secret);
        return Arrays.equals(expected, actual);
    }

    /**
     * Hashes the password using Argon2 with the specified salt and secret.
     *
     * @param password the raw password to hash
     * @param salt     the salt to use for hashing
     * @param secret   the secret key to enhance security
     * @return the hashed password as a byte array
     */
    private byte[] hashPassword(String password, byte[] salt, byte[] secret) {
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        Argon2Parameters.Builder parameters = new Argon2Parameters.Builder()
                .withSalt(salt)
                .withSecret(secret)
                .withParallelism(argon2Properties.parallelism())
                .withMemoryAsKB(argon2Properties.memory())
                .withIterations(argon2Properties.iterations());

        generator.init(parameters.build());
        byte[] hash = new byte[argon2Properties.hashLength()];
        generator.generateBytes(password.toCharArray(), hash);
        return hash;
    }

    /**
     * Retrieves the salt to use in hashing
     *
     * @return the salt as a byte array
     */
    private byte[] getSalt() {
        return toUtf8Bytes(argon2Properties.salt());
    }

    /**
     * Retrieves the secret key to use in hashing, encoded as UTF-8 bytes.
     *
     * @return the secret key as a byte array
     */
    private byte[] getSecretKey() {
        return toUtf8Bytes(argon2Properties.secretKey());
    }

    private byte[] toUtf8Bytes(char[] chars) {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        try {
            ByteBuffer byteBuffer = encoder.encode(charBuffer);
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            return bytes;
        } catch (CharacterCodingException ex) {
            throw new IllegalArgumentException("Failed to encode chars to bytes", ex);
        }
    }
}