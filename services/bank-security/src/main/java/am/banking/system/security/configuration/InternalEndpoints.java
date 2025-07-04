package am.banking.system.security.configuration;

import java.util.stream.Stream;

/**
 * Author: Artyom Aroyan
 * Date: 04.07.25
 * Time: 14:37:43
 */
final class InternalEndpoints {
    static final String[] JWT_TOKEN = {
            "/api/v1/secure/local/system-token",
            "/api/internal/security/system/token",
            "/api/internal/security/jwt/generate",
            "/api/internal/security/jwt/validate",
            "/api/internal/security/password/hash",
            "/api/internal/security/password/validate",
            "/api/internal/security/user-token/email/issue",
            "/api/internal/security/user-token/email/validate",
            "/api/internal/security/user-token/password-reset/issue",
            "/api/internal/security/user-token/password-reset/validate",
            "/api/internal/security/token/invalidate",
            "/api/internal/security/authorize",
    };

    static final String[] NOTIFICATION = {
            "/api/notification/email-verification",
            "/api/notification/password-reset",
            "/api/notification/welcome-email",
    };

    static final String[] ALL = Stream.of(JWT_TOKEN, NOTIFICATION)
            .flatMap(Stream::of)
            .toArray(String[]::new);
}