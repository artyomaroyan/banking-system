package am.banking.system.security.token.service;

import am.banking.system.security.exception.StrategyNotFoundException;
import am.banking.system.security.model.enums.TokenType;
import am.banking.system.security.token.service.abstraction.ITokenService;
import am.banking.system.security.token.factory.TokenStrategyFactory;
import am.banking.system.security.token.strategy.TokenGenerationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:33:40
 */
@Service
@RequiredArgsConstructor
class TokenService implements ITokenService {
    private final TokenStrategyFactory tokenStrategyFactory;

    @Override
    public String createToken(Map<String, Object> claims, String subject, TokenType type) {
        return Optional.ofNullable(tokenStrategyFactory.getTokenGenerationStrategy(type))
                .map(strategy -> strategy.generateToken(claims, subject))
                .orElseThrow(() -> new StrategyNotFoundException("No strategy found for type " + type));
    }

    @Override
    public String createSystemToken(TokenType type) {
        return Optional.ofNullable(tokenStrategyFactory.getTokenGenerationStrategy(type))
                .map(TokenGenerationStrategy::generateSystemToken)
                .orElseThrow(() -> new StrategyNotFoundException("No strategy found for type " + type));
    }
}