package am.banking.system.common.shared.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 15:31:42
 */
@Validated
public record AccountCreationRequest(@NotNull Integer userId, @NotBlank String username) {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccountCreationRequest that = (AccountCreationRequest) obj;
        return Objects.equals(this.userId, that.userId) &&
                Objects.equals(this.username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username);
    }
}