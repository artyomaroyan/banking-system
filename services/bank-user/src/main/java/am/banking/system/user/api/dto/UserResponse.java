package am.banking.system.user.api.dto;

import am.banking.system.common.shared.enums.AccountState;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:11:53
 */
public record UserResponse(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt, String username,
                           String firstName, String lastName, String email, String phone, Integer age, AccountState state) {
}