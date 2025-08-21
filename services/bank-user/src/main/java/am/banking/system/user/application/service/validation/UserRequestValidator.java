package am.banking.system.user.application.service.validation;

import am.banking.system.common.shared.response.ValidationResult;
import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.infrastructure.adapter.out.persistence.UserRepository;
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
 * Date: 14.04.25
 * Time: 18:18:24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserRequestValidator implements RequestValidation<UserRequest> {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{9,12}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9]{3,20}$");
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
                .flatMap(result -> Flux.fromIterable(result.message()))
                .collectList()
                .map(messages -> messages.isEmpty()
                ? ValidationResult.valid()
                        : ValidationResult.invalid());
    }

    private Mono<ValidationResult> isValidUsername(String username) {
        List<String> errors = new ArrayList<>();

        if (!isValidUsernameLength(username)) {
            errors.add("Username must be between 3 and 20 characters");
        }

        if (!isValidUsernameFormat(username)) {
            errors.add("Username must include uppercase, lowercase and number");
        }

        if (errors.isEmpty()) {
            return Mono.just(ValidationResult.valid());
        } else  {
            log.error("Username validation failed '{}'", username);
            return Mono.just(ValidationResult.invalid(errors));
        }
    }

    private Mono<ValidationResult> isValidPassword(String password) {
        List<String> errors = new ArrayList<>();

        if (!isValidPasswordLength(password)) {
            errors.add("Password must be between 8 and 20 characters.");
        }

        if (!isValidPasswordFormat(password)) {
            errors.add("Password must include uppercase, lowercase, number, and special character.");
        }

        if (errors.isEmpty()) {
            return Mono.just(ValidationResult.valid());
        } else {
            log.error("Password validation failed: {}", errors);
            return Mono.just(ValidationResult.invalid(errors));
        }
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

    private Mono<ValidationResult> isValidPhoneNumber(String number) {
        List<String> errors = new ArrayList<>();

        if (!isValidPhoneNumberLength(number)) {
            errors.add("Phone number must be between 9 and 12 characters");
        }

        if (!isValidPhoneNumberFormat(number)) {
            errors.add("Phone number must include numeric characters");
        }

        if (errors.isEmpty()) {
            return Mono.just(ValidationResult.valid());
        } else  {
            log.error("Phone number validation failed: {}", errors);
            return Mono.just(ValidationResult.invalid(errors));
        }
    }

    private boolean isValidUsernameLength(String username) {
        return username.length() >= 3 && username.length() <= 20;
    }

    private boolean isValidUsernameFormat(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
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

    private boolean isValidPhoneNumberFormat(String number) {
        return PHONE_PATTERN.matcher(number).matches();
    }

    private boolean isValidPhoneNumberLength(String number) {
        return number.length() >= 9 && number.length() <= 12;
    }
}