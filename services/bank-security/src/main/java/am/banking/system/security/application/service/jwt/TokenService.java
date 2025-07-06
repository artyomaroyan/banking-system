package am.banking.system.security.application.service.jwt;

import am.banking.system.common.shared.exception.security.token.StrategyNotFoundException;
import am.banking.system.security.application.port.in.TokenGenerationUseCase;
import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.application.token.factory.TokenStrategyFactory;
import am.banking.system.security.application.token.strategy.TokenGenerationStrategy;
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
public class TokenService implements TokenGenerationUseCase {
    private final TokenStrategyFactory tokenStrategyFactory;

    @Override
    public String createToken(Map<String, Object> claims, String subject, TokenType type) {
        return Optional.ofNullable(tokenStrategyFactory.getStrategy(type))
                .map(strategy -> strategy.generateToken(claims, subject))
                .orElseThrow(() -> new StrategyNotFoundException("No strategy found for type " + type));
    }

    @Override
    public String generate(TokenType type) {
        return Optional.ofNullable(tokenStrategyFactory.getStrategy(type))
                .map(TokenGenerationStrategy::generateSystemToken)
                .orElseThrow(() -> new StrategyNotFoundException("No strategy found for type " + type));
    }
}