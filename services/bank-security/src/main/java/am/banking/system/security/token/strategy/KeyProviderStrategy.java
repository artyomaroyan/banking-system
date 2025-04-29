package am.banking.system.security.token.strategy;

import java.security.Key;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 00:50:06
 */
public interface KeyProviderStrategy {
    Key getPrivateKey();
    Key getPublicKey();
    Long getExpiration();
}