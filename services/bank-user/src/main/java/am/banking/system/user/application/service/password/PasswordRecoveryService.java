package am.banking.system.user.application.service.password;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.PasswordResetConfirmRequest;
import am.banking.system.user.api.dto.PasswordResetEmailRequest;
import am.banking.system.user.application.port.in.password.PasswordRecoveryFactoryUserCase;
import am.banking.system.user.application.port.in.password.PasswordRecoveryUseCase;
import am.banking.system.user.application.service.validation.PasswordRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final PasswordRecoveryFactoryUserCase passwordRecoveryFactory;

    @Override
    public Mono<Result<String>> resetPassword(PasswordResetConfirmRequest request) {
        return requestValidator.isValidPassword(request.newPassword())
                .flatMap(errors -> {
                    if (!errors.message().isEmpty()) {
                        String errorMessage = String.join(", ", errors.message());
                        log.error("Invalid password format: {}", errorMessage);
                        return Mono.just(Result.error("Please enter valid password", BAD_REQUEST.value()));
                    }

                    return passwordRecoveryFactory.completePasswordReset(request)
                            .then(Mono.just(Result.success("Password successfully reset")));

                    // todo: add password token deactivation method, when use password mark it as USED and deactivate it.
                });
    }

    @Override
    public Mono<Result<String>> sendPasswordResetEmail(PasswordResetEmailRequest request) {
        log.info("Initiating password recovery for email: {}", request.email());
        return requestValidator.isValidRequest(request)
                .flatMap(errors -> {
                    if (!errors.message().isEmpty()) {
                        String errorMessage = String.join(", ", errors.message());
                        log.error("Password reset request validation failed: {}", errorMessage);
                        return Mono.just(Result.error("Password reset request validation failed", BAD_REQUEST.value()));
                    }

                    return passwordRecoveryFactory.sendPasswordResetEmail(request)
                            .then(Mono.just(Result.success("Password reset verification link successfully send to your email")));
                });
    }
}