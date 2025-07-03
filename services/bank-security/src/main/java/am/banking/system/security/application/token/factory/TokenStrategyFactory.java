package am.banking.system.security.application.token.factory;

import am.banking.system.security.domain.enums.TokenType;
import am.banking.system.security.application.token.strategy.TokenGenerationStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 01:34:37
 */
@Component
public class TokenStrategyFactory {
    private final Map<TokenType, TokenGenerationStrategy> strategy;

    public TokenStrategyFactory(List<TokenGenerationStrategy> strategy) {
        this.strategy = strategy.stream().collect(Collectors.toMap(TokenGenerationStrategy::getSupportedTokenType, Function.identity()));
    }

    public TokenGenerationStrategy getStrategy(TokenType type) {
        return strategy.get(type);
    }
}