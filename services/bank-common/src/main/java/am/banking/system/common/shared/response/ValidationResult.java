package am.banking.system.common.shared.response;

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

    public static ValidationResult invalid(String... message) {
        return new ValidationResult(false, List.of(message));
    }

    public static ValidationResult invalid(List<String> message) {
        return new ValidationResult(false, message);
    }
}