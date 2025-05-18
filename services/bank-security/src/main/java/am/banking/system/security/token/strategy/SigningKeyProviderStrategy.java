package am.banking.system.security.token.strategy;

import am.banking.system.security.model.enums.TokenType;
import am.banking.system.security.token.dto.SigningCredentials;

import java.security.Key;

/**
 * Author: Artyom Aroyan
 * Date: 18.04.25
 * Time: 00:51:40
 */
public interface SigningKeyProviderStrategy<K extends Key> {
    SigningCredentials<K> signingCredentials();
    TokenType getTokenType();
    Long getTokenExpiration();
}