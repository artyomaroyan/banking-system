package am.banking.system.security.token.strategy;

import am.banking.system.security.model.enums.TokenType;

import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:35:25
 */
public interface TokenGenerationStrategy {
    String generateToken(Map<String, Object> claims, String subject);
    TokenType getSupportedTokenType();
}