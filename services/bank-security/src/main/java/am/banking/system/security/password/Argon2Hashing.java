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

    private byte[] getSalt() {
        return toUtf8Bytes(argon2Properties.salt());
    }

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