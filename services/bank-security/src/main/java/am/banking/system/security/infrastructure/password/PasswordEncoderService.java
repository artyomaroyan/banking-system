package am.banking.system.security.infrastructure.password;

import am.banking.system.security.application.port.out.CustomPasswordEncoder;
import jakarta.annotation.PreDestroy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Author: Artyom Aroyan
 * Date: 05.07.25
 * Time: 15:10:23
 */
@Service
public class PasswordEncoderService implements CustomPasswordEncoder {
    private final char[] pepper;
    private final Argon2PasswordEncoder passwordEncoder;

    public PasswordEncoderService(Argon2Properties properties, Argon2PasswordEncoder passwordEncoder) {
        this.pepper = properties.pepper();
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword + new String(pepper));
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword + new String(pepper), encodedPassword);
    }

    @PreDestroy
    void clearPepper() {
        Arrays.fill(pepper, '\0');
    }
}