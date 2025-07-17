package am.banking.system.common.shared.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 15:31:42
 */
@Validated
public record UserRegistrationEvent(@NotNull Integer userId, @NotBlank String username, @Email @NotBlank String email,
                                    @NotBlank String firstName, @NotBlank String lastName, @NotBlank String phone) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserRegistrationEvent that = (UserRegistrationEvent) obj;
        return Objects.equals(this.userId, that.userId) &&
                Objects.equals(this.username, that.username) &&
                Objects.equals(this.email, that.email) &&
                Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, email, firstName, lastName, phone);
    }

    @NonNull
    @Override
    public String toString() {
        return "UserRegistrationEvent{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}