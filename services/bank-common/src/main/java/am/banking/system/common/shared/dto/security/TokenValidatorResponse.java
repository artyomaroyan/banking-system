package am.banking.system.common.shared.dto.security;

/**
 * Author: Artyom Aroyan
 * Date: 05.07.25
 * Time: 17:22:07
 */
public record TokenValidatorResponse(boolean valid, String reason) {

    public static TokenValidatorResponse success() {
        return new TokenValidatorResponse(true, null);
    }

    public static TokenValidatorResponse failure(String reason) {
        return new TokenValidatorResponse(false, reason);
    }
}