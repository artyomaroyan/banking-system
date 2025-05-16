package am.banking.system.user.service.validation;

import am.banking.system.user.model.dto.UserRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 18:17:50
 */
public interface RequestValidation {
    Mono<List<String>> validateRequest(UserRequest request);
    Mono<ValidationResult> isValidUsername(@NotBlank String username);
    Mono<ValidationResult> isValidPassword(@NotBlank String password);
    Mono<ValidationResult> isValidEmail(@Email @NotBlank String email);
    Mono<ValidationResult> isValidPhoneNumber(@NotBlank String number);
}