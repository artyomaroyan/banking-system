package am.banking.system.security.application.port.in;

import am.banking.system.security.domain.enums.TokenType;

import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:08:49
 */
public interface TokenGenerationUseCase {
    String createToken(Map<String, Object> claims, String subject, TokenType type);
    String generate(TokenType type);
}