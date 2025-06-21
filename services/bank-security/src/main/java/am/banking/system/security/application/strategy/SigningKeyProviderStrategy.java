package am.banking.system.security.application.strategy;

import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.api.dto.SigningCredentials;

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