package am.banking.system.user.application.service.validation;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 19:00:20
 */
public record ValidationResult(boolean isValid, String message) {

    public static ValidationResult valid() {
        return new ValidationResult(true, "valid");
    }

    public static ValidationResult invalid(String message) {
        return new ValidationResult(false, message);
    }
}