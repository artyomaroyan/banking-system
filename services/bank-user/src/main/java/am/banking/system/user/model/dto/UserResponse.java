package am.banking.system.user.model.dto;

import am.banking.system.common.enums.AccountState;

import java.util.Date;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:11:53
 */
public record UserResponse(Long id, Date createdAt, Date updatedAt, String username, String firstName,
                           String lastName, String email, String password, String phone, Integer age, AccountState state) {
}