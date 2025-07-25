package am.banking.system.user.application.service.password;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.PasswordResetRequest;
import am.banking.system.user.application.port.in.password.PasswordRecoveryUseCase;
import am.banking.system.user.application.service.validation.PasswordRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
        log.info("Initiating password recovery for email: {}", request.email());
        return requestValidator.isValidRequest(request)
                .flatMap(errors -> {
                    if (!errors.message().isEmpty()) {
                        String errorMessage = String.join(", ", errors.message());
                        log.error("Password recovery request validation failed: {}: {}", request.email(), errorMessage);
                        return Mono.just(Result.error(errorMessage, BAD_REQUEST.value()));
                    }
                    log.info("Password recovery request validation success");

                    return
                })
    }
}