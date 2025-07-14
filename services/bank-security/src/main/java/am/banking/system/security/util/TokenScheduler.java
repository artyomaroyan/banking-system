package am.banking.system.security.util;

import am.banking.system.security.application.port.in.InvalidateTokenUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Author: Artyom Aroyan
 * Date: 25.04.25
 * Time: 00:56:50
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenScheduler {
    private final InvalidateTokenUseCase invalidateTokenService;

    @Scheduled(fixedRate = 900000)
    public void init() {
        invalidateTokenService.markTokensForciblyExpired()
                .doOnNext(count -> log.info("Tokens marked as forcibly expired: {}", count))
                .doOnError(error -> log.error("Failed to mark tokens for expired: {}", error.getMessage()))
                .subscribe();
    }
}