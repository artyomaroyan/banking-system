package am.banking.system.user.model.result;

import lombok.Builder;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:58:20
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