package am.banking.system.security.token.strategy;

import am.banking.system.security.model.enums.TokenType;

import java.security.Key;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 00:51:40
 */
public interface SigningKeyProviderStrategy {
    Key getSigningKey();
    TokenType getTokenType();
    Long getTokenExpiration();
}