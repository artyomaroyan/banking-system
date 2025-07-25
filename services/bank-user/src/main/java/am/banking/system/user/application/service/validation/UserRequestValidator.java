package am.banking.system.user.application.service.validation;

import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 18:18:24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserRequestValidator implements RequestValidation<UserRequest> {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{9,12}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&.,])[A-Za-z\\d@$!%*?&.,]{8,20}$");

    private final UserRepository userRepository;

    @Override
    public Mono<ValidationResult> isValidRequest(UserRequest request) {
        return Flux.merge(
                        isValidUsername(request.username()),
                        isValidPassword(request.password()),
                        isValidEmail(request.email()),
                        isValidPhoneNumber(request.phone())
                )
                .filter(result -> !result.isValid())
                .map(ValidationResult::message)
                .reduce(new ArrayList<>(), (all, next) -> {
                    all.addAll(next);
                    return all;
                })
                .map(errors -> errors.isEmpty() ?
                        ValidationResult.valid() : ValidationResult.invalid(String.valueOf(errors)));
    }

    private Mono<ValidationResult> isValidUsername(String username) {
        return userRepository.existsByUsername(username)
                .map(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        String message = "Username " + username + " is already in use";
                        log.error(message);
                        return ValidationResult.invalid(message);
                    }
                    return ValidationResult.valid();
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

    private Mono<ValidationResult> isValidEmail(String email) {
        return userRepository.existsByEmail(email)
                .map(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        String message = "Email " + email + " is already in use";
                        log.error(message);
                        return ValidationResult.invalid(message);
                    }
                    if (!EMAIL_PATTERN.matcher(email).matches()) {
                        String message = "Email " + email + " is not a valid email address";
                        log.error(message);
                        return ValidationResult.invalid(message);
                    }
                    return ValidationResult.valid();
                });
    }

    private Mono<ValidationResult> isValidPhoneNumber(String number) {
        if (number.length() < 9 || number.length() > 12) {
            String message = "Phone number must be between 9 and 12 characters";
            log.error(message);
            return Mono.just(ValidationResult.invalid(message));
        }
        if (!PHONE_PATTERN.matcher(number).matches()) {
            String message = "Invalid phone number";
            log.error(message);
            return Mono.just(ValidationResult.invalid(message));
        }
        return Mono.just(ValidationResult.valid());
    }
}