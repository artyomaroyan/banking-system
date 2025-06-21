package am.banking.system.security.application.token.strategy;

import am.banking.system.security.domain.enums.TokenType;

import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:35:25
 */
public interface TokenGenerationStrategy {
    default String generateToken(Map<String, Object> claims, String subject) {
        throw new UnsupportedOperationException("Generate system token is not supported");
    }

    default String generateSystemToken() {
        throw new UnsupportedOperationException("Generate system token is not supported");
    }

    TokenType getSupportedTokenType();
}