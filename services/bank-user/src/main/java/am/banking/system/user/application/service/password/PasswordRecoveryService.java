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
        return null;
    }

    @Override
    public Mono<Result<String>> sendPasswordResetEmail(PasswordResetRequest request) {
        log.info("Initiating password recovery for email: {}", request.email());
        return requestValidator.isValidRequest(request)
                .flatMap(errors -> {
                    if (!errors.message().isEmpty()) {
                        String errorMessage = String.join(", ", errors.message());
                        log.error("Password reset request validation failed: {}", errorMessage);
                        return Mono.just(Result.error("Password reset request validation failed", BAD_REQUEST.value()));
                    }

                    return passwordRecoveryFactory.sendPasswordResetEmail(request)
                            .thenReturn(Result.success("Password reset email sent successfully"))
                            .doOnSuccess(_ -> log.info("Password reset link has been sent to: {}", request.email()))
                            .onErrorResume(ex -> {
                                log.error("Failed to send password reset email to {}", request.email(), ex);
                                return Mono.just(Result.error("Failed to send password reset email", INTERNAL_SERVER_ERROR.value()));
                            });
                });
    }
}