package am.banking.system.user.application.factory;

import am.banking.system.user.api.dto.PasswordResetRequest;
import am.banking.system.user.application.port.in.password.PasswordRecoveryFactoryUserCase;
import am.banking.system.user.application.port.out.UserTokenClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 25.07.25
 * Time: 21:34:52
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordRecoveryFactory implements PasswordRecoveryFactoryUserCase {
    private final UserTokenClientPort userTokenClient;

    @Override
    public Mono<String> resetPassword(PasswordResetRequest request) {
        return null;
    }
}