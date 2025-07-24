package am.banking.system.user.application.service.validation;

import am.banking.system.user.api.dto.PasswordResetRequest;
import am.banking.system.user.application.port.out.UserTokenClientPort;
import am.banking.system.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Author: Artyom Aroyan
 * Date: 24.07.25
 * Time: 22:42:41
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordRequestValidator implements RequestValidation<PasswordResetRequest> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&.,])[A-Za-z\\d@$!%*?&.,]{8,20}$");

    private final UserRepository userRepository;
    private final UserTokenClientPort userTokenClient;

    @Override
    public Mono<ValidationResult> isValidRequest(PasswordResetRequest request) {
        return null;
    }

    private Mono<ValidationResult> isValidToken(String recoveryToken) {
        return userTokenClient.validatePasswordRecoveryToken()
    }

    private Mono<ValidationResult> isValidEmail(String email) {
        return userRepository.existsByEmail(email)
                .map(exists -> {
                    if (!EMAIL_PATTERN.matcher(email).matches()) {
                        String message = "Email " + email + " is not a valid email address";
                        log.error(message);
                        return ValidationResult.invalid(message);
                    }
                    if (Boolean.FALSE.equals(exists)) {
                        String message = "User with " + email + " does not exist";
                        log.error(message);
                        return ValidationResult.invalid(message);
                    }
                    return Mono.just(ValidationResult.valid());
                });
    }

    private Mono<ValidationResult> isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 20) {
            String message = "Password must be between 8 and 20 characters";
            log.error(message);
            return Mono.just(ValidationResult.invalid(message));
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            String message = "Password must contain uppercase, lowercase, number, special char";
            log.error(message);
            return Mono.just(ValidationResult.invalid(message));
        }
        return Mono.just(ValidationResult.valid());
    }
}