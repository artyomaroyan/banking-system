package am.banking.system.user.application.service.password;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.PasswordResetRequest;
import am.banking.system.user.application.port.in.password.PasswordRecoveryUseCase;
import am.banking.system.user.application.service.validation.PasswordRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 24.07.25
 * Time: 22:34:10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordRecoveryService implements PasswordRecoveryUseCase {
    private final PasswordRequestValidator requestValidator;

    @Override
    public Mono<Result<String>> resetPassword(PasswordResetRequest request) {
        return null;
    }
}