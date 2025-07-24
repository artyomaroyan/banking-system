package am.banking.system.user.application.service.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 19:00:20
 */
public record ValidationResult(boolean isValid, List<String> message) {

    public static ValidationResult valid() {
        return new ValidationResult(true, List.of());
    }

    public static ValidationResult invalid(String message) {
        return new ValidationResult(false, List.of(message));
    }

    public static ValidationResult invalid(List<String> message) {
        return new ValidationResult(false, message);
    }

    public ValidationResult merge(ValidationResult other) {
        if (this.isValid != other.isValid) return valid();
        List<String> combined =  new ArrayList<>(this.message);
        combined.addAll(other.message);
        return invalid(combined);
    }
}