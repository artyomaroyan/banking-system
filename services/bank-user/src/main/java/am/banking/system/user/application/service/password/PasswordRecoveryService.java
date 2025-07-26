package am.banking.system.user.application.service.password;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.PasswordResetRequest;
import am.banking.system.user.application.port.in.password.PasswordRecoveryFactoryUserCase;
import am.banking.system.user.application.port.in.password.PasswordRecoveryUseCase;
import am.banking.system.user.application.service.validation.PasswordRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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
    private final PasswordRecoveryFactoryUserCase passwordRecoveryFactory;

    @Override
    public Mono<Result<String>> resetPassword(PasswordResetRequest request) {
        log.info("Initiating password recovery for email: {}", request.email());
        return requestValidator.isValidRequest(request)
                .flatMap(errors -> {
                    if (!errors.message().isEmpty()) {
                        String errorMessage = String.join(", ", errors.message());
                        log.error("Password recovery request validation failed: {}: {}", request.email(), errorMessage);
                        return Mono.just(Result.<String>error(errorMessage, BAD_REQUEST.value()));
                    }
                    log.info("Password recovery request validation success");

                    return passwordRecoveryFactory.resetPassword(request)
                            .map(token -> Result.<String>success(token.token()))
                            .doOnSuccess(_ -> log.info("Password recovery token generated for {} ", request.email()))
                            .doOnError(_ -> log.error("Failed to generate password recovery token for {}", request.email()));
                })
                .onErrorResume(error -> {
                    log.error("Password reset failed for {}: {}", request.email(), error.getMessage());
                    return Mono.just(Result.error("Password recovery failed. Please try again later.", INTERNAL_SERVER_ERROR.value()));
                });
    }
}