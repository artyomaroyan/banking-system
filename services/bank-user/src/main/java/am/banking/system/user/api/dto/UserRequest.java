package am.banking.system.user.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 20:40:22
 */
public record UserRequest(
        @Schema(example = "user1")
        @NotBlank(message = "username is required")
        @Size(message = "username must be between 8 - 30 characters", min = 3, max = 30)
        String username,

        @Schema(example = "user1")
        @NotNull(message = "first name is required")
        @Size(message = "first name must be between 3 - 50 letters", min = 3, max = 50)
        String firstName,

        @Schema(example = "user1")
        @NotNull(message = "last name is required")
        @Size(message = "last name must be between 3 - 50 letters", min = 3, max = 50)
        String lastName,

        @Email
        @Schema(example = "example@gmail.com")
        @NotBlank(message = "email is required")
        @Size(message = "email must be between 15 - 50 characters", min = 15, max = 50)
        String email,

        @Schema(example = "Password.1")
        @NotBlank(message = "password is required")
        @Size(message = "password must be between 8 - 30 characters", min = 8, max = 30)
        String password,

        @Schema(example = "+37412345678")
        @NotBlank(message = "phone number is required")
        @Size(message = "phone number must be between 9 - 12 numbers", min = 9, max = 12)
        String phone,

        @Min(18)
        @Max(100)
        @NotNull(message = "age is required")
        Integer age) {
}