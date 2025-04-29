package am.banking.system.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 20:40:22
 */
public record UserRequest(
        @NotBlank(message = "username is required")
        String username,
        @NotNull(message = "first name is required")
        String firstName,
        @NotNull(message = "last name is required")
        String lastName,
        @Email
        @NotBlank(message = "email is required")
        String email,
        @NotBlank(message = "password is required")
        String password,
        @NotBlank(message = "password is required")
        String phone,
        @NotNull(message = "password is required")
        Integer age) {
}