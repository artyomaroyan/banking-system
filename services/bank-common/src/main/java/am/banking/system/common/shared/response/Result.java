package am.banking.system.common.shared.response;

import lombok.Builder;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 16:03:05
 */
@Builder
public record Result<T> (T data, boolean success, String message, Integer errorCode) {

    public static <T> Result<T> success(T data, String message) {
        return Result.<T>builder()
                .data(data)
                .success(true)
                .message(defaultMessage(message))
                .build();
    }

    public static <T> Result<T> success(String message) {
        return success(null, message);
    }

    public static Result<String> successMessage(String message) {
        return success(message);
    }

    public static <T> Result<T> error(String message, Integer errorCode) {
        return Result.<T>builder()
                .data(null)
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .build();
    }

    private static String defaultMessage(String message) {
        return message == null || message.isBlank() ? "" : message;
    }
}