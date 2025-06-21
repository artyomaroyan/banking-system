package am.banking.system.security.util;

import am.banking.system.security.application.port.in.UserTokenServiceUseCase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Author: Artyom Aroyan
 * Date: 25.04.25
 * Time: 00:56:50
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenScheduler {
    private final UserTokenServiceUseCase userTokenService;

    @PostConstruct
    public void init() {
        Flux.interval(Duration.ofMinutes(15))
                .flatMap(tick -> userTokenService.markTokensForciblyExpired())
                .doOnNext(count -> log.info("Tokens marked as forcibly expired: {}", count))
                .doOnError(error -> log.error("Failed to mark tokens for expired: {}", error.getMessage()))
                .subscribe();
    }
}