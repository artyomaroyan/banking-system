package am.banking.system.security.api.shared;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import java.security.Key;

/**
 * Author: Artyom Aroyan
 * Date: 17.05.25
 * Time: 19:58:17
 */
public interface SigningCredentials<K extends Key> {
    K key();

    SecureDigestAlgorithm<K, ?> algorithm();

    default JwtBuilder sign(JwtBuilder builder) {
        return builder.signWith(key(), algorithm());
    }
}