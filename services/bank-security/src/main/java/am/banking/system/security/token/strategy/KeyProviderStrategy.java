package am.banking.system.security.token.strategy;

import java.security.Key;
import java.security.PrivateKey;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 00:50:06
 */
public interface KeyProviderStrategy {
    PrivateKey getPrivateKey();
    Key getPublicKey();
    Long getExpiration();
    String getKeyId();
}