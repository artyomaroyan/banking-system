package am.banking.system.security.token.dto;

import io.jsonwebtoken.security.SecureDigestAlgorithm;

import java.security.PrivateKey;

/**
 * Author: Artyom Aroyan
 * Date: 17.05.25
 * Time: 19:42:40
 */
public record AsymmetricSigningCredentials(PrivateKey key, SecureDigestAlgorithm<PrivateKey, ?> algorithm) implements SigningCredentials<PrivateKey> {
}