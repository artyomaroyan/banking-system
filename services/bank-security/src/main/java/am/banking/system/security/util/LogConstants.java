package am.banking.system.security.util;

/**
 * Author: Artyom Aroyan
 * Date: 19.04.25
 * Time: 02:00:10
 */
public class LogConstants {
    public static final String TOKEN_NOT_FOUND = "Token was not found";
    public static final String INVALID_TOKEN_PURPOSE = "Invalid token purpose";
    public static final String INVALID_TOKEN_STATE = "Invalid token state";
    public static final String EXPIRED_TOKEN = "Expired token, {}";
    public static final String TOKEN_VALIDATION_SUCCESS = "Token successfully validated";
    public static final String INVALID_TOKEN_SIGNATURE = "Invalid token signature, {}";
    public static final String UNSUPPORTED_TOKEN = "Unsupported token, {}";
    public static final String VALIDATION_FAILED = "Validation failed, unknown error, {}";
    public static final String START_SCHEDULE = "Running scheduled task to update expired tokens...";
    public static final String FINISH_SCHEDULE = "Finished scheduled task. {} tokens marked as FORCIBLY_EXPIRED";
}