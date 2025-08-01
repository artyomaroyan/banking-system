package am.banking.system.security.application.service.jwt;

import am.banking.system.security.application.port.in.InvalidateTokenUseCase;
import am.banking.system.security.domain.enums.TokenState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static am.banking.system.security.domain.enums.TokenState.*;
import static am.banking.system.security.domain.enums.TokenState.PENDING;

/**
 * Author: Artyom Aroyan
 * Date: 14.07.25
 * Time: 00:06:58
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvalidateTokenService implements InvalidateTokenUseCase {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Mono<Long> markTokensForciblyExpired() {
        return updateTokenState(FORCIBLY_EXPIRED, null);
    }

    @Override
    public Mono<Long> markTokenAsVerified(String token) {
        return updateTokenState(VERIFIED, token);
    }
    // todo: add method to check user account, if it is active mark JWT access token state as VALID,
    //  and method which check if JWT access token is expired mark token state as EXPIRED.

    private Mono<Long> updateTokenState(TokenState to, @Nullable String tokenFilter) {
        StringBuilder sql = new StringBuilder("""
                UPDATE security.user_token
                SET token_state = :to
                WHERE token_state = :from
                """);

        if (tokenFilter != null) {
            sql.append(" AND token = :token");
        } else {
            sql.append(" AND expires_at < CURRENT_TIMESTAMP");
        }

        var spec = r2dbcEntityTemplate.getDatabaseClient()
                .sql(sql.toString())
                .bind("to", to.name())
                .bind("from", PENDING.name());

        if (tokenFilter != null) {
            spec = spec.bind("token", tokenFilter);
        }

        return spec
                .fetch()
                .rowsUpdated()
                .doOnNext(count -> {
                    if (count == 0) {
                        log.warn("No tokens were updated from PENDING to {}. Possible reasons: token not found or already expired", to);
                    } else {
                        log.info("Updated {} token(s) from PENDING to {}", count, to);
                    }
                });
    }
}