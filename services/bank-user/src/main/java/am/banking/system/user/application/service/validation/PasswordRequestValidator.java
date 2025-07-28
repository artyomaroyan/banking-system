package am.banking.system.user.application.service.validation;

import am.banking.system.common.shared.response.ValidationResult;
import am.banking.system.user.api.dto.PasswordResetEmailRequest;
import am.banking.system.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
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
public class PasswordRequestValidator implements RequestValidation<PasswordResetEmailRequest> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&.,])[A-Za-z\\d@$!%*?&.,]{8,20}$");

    private final UserRepository userRepository;

    @Override
    public Mono<ValidationResult> isValidRequest(PasswordResetEmailRequest request) {
        return Flux.merge(
                        isValidUserId(request.userId()),
                        isValidEmail(request.email())
                )
                .filter(result -> !result.isValid())
                .flatMap(result -> Flux.fromIterable(result.message()))
                .collectList()
                .map(messages -> messages.isEmpty()
                        ? ValidationResult.valid()
                        : ValidationResult.invalid());
    }

    public Mono<ValidationResult> isValidPassword(String password) {
        List<String> errors = new ArrayList<>();

        if (!isValidPasswordLength(password)) {
            errors.add("Password must be between 8 and 20 characters.");
        }

        if (!isValidPasswordFormat(password)) {
            errors.add("Password must include uppercase, lowercase, number, and special character.");
        }

        if (!errors.isEmpty()) {
            return Mono.just(ValidationResult.valid());
        } else {
            log.error("Password validation failed: {}", errors);
            return Mono.just(ValidationResult.invalid(errors));
        }
    }

    private Mono<ValidationResult> isValidUserId(Integer userId) {
        return userRepository.existsById(userId)
                .map(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        String message = String.format("User with id %d does not exist", userId);
                        log.error(message);
                        return ValidationResult.invalid(message);

                    }
                    return ValidationResult.valid();
                });
    }

    private Mono<ValidationResult> isValidEmail(String email) {
        List<String> errors = new ArrayList<>();

        if (!isValidEmailFormat(email)) {
            errors.add("Email '%s' is not valid".formatted(email));
        }

        return userRepository.existsByEmail(email)
                .map(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        errors.add("User with email '%s' does not exist".formatted(email));
                    }

                    return errors.isEmpty()
                            ? ValidationResult.valid()
                            : ValidationResult.invalid(errors);
                });
    }

    private boolean isValidPasswordLength(String password) {
        return password.length() >= 8 && password.length() <= 20;
    }

    private boolean isValidPasswordFormat(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    private boolean isValidEmailFormat(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}