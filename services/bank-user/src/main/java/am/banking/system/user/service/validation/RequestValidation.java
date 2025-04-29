package am.banking.system.user.service.validation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 18:17:50
 */
public interface RequestValidation {
    ValidationResult isValidUsername(@NotBlank String username);
    ValidationResult isValidPassword(@NotBlank String password);
    ValidationResult isValidEmail(@Email @NotBlank String email);
    ValidationResult isValidPhoneNumber(@NotBlank String number);
}