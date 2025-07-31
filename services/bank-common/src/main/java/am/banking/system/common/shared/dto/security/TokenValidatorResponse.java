package am.banking.system.common.shared.dto.security;

/**
 * Author: Artyom Aroyan
 * Date: 05.07.25
 * Time: 17:22:07
 */
public record TokenValidatorResponse(boolean success, String reason) {

    public static TokenValidatorResponse valid() {
        return new TokenValidatorResponse(true, null);
    }

    public static TokenValidatorResponse invalid(String reason) {
        return new TokenValidatorResponse(false, reason);
    }
}