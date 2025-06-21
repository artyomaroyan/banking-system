package am.banking.system.common.shared.dto;

import java.time.LocalDateTime;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:09:54
 */
public record ErrorResponse(String code, String message, LocalDateTime timestamp) {
}