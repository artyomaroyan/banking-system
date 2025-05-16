package am.banking.system.security.token.service.abstraction;

import am.banking.system.security.model.enums.TokenType;

import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:08:49
 */
public interface ITokenService {
    String createToken(Map<String, Object> claims, String subject, TokenType type);
    String createSystemToken(TokenType type);
}