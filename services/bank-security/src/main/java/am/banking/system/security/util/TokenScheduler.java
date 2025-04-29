package am.banking.system.security.util;

import am.banking.system.security.model.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Artyom Aroyan
 * Date: 25.04.25
 * Time: 00:56:50
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class TokenScheduler {
    private final UserTokenRepository userTokenRepository;

    @Transactional
    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void schedule() {
        log.info(LogConstants.START_SCHEDULE);
        int expiredTokens = userTokenRepository.markTokensForciblyExpired();
        log.info(LogConstants.FINISH_SCHEDULE, expiredTokens);
    }
}