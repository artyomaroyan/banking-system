package am.banking.system.security.token.dto;

import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;

/**
 * Author: Artyom Aroyan
 * Date: 17.05.25
 * Time: 19:56:48
 */
public record HmacSigningCredentials(SecretKey key, SecureDigestAlgorithm<SecretKey, ?> algorithm) implements SigningCredentials<SecretKey> {
}