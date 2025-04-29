package am.banking.system.user.service.validation;

import am.banking.system.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 18:18:24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RequestValidator implements RequestValidation {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&.,])[A-Za-z\\d@$!%*?&.,]{8,20}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{9,12}$");

    private final UserRepository userRepository;

    @Override
    public ValidationResult isValidUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            String message = "Username is already in use" + username;
            log.error(message);
            return ValidationResult.invalid(message);
        }
        return ValidationResult.valid();
    }

    @Override
    public ValidationResult isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 20) {
            String message = "Password must be between 8 and 20 characters";
            log.error(message);
            return ValidationResult.invalid(message);
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            String message = "Password must contain uppercase, lowercase, number, special char";
            log.error(message);
            return ValidationResult.invalid(message);
        }
        return ValidationResult.valid();
    }

    @Override
    public ValidationResult isValidEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            String message = "Email already exists: " + email;
            log.error(message);
            return ValidationResult.invalid(message);
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            String message = "Invalid email format";
            log.error(message);
            return ValidationResult.invalid(message);
        }
        return ValidationResult.valid();
    }

    @Override
    public ValidationResult isValidPhoneNumber(String number) {
        if (number.length() < 9 || number.length() > 12) {
            String message = "Phone number must be between 9 and 12 characters";
            log.error(message);
            return ValidationResult.invalid(message);
        }
        if (!PHONE_PATTERN.matcher(number).matches()) {
            String message = "Invalid phone number";
            log.error(message);
            return ValidationResult.invalid(message);
        }
        return ValidationResult.valid();
    }
}