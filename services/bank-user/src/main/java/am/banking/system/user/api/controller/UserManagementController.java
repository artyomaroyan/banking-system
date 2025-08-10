package am.banking.system.user.api.controller;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.UserResponse;
import am.banking.system.user.application.port.in.user.UserManagementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 30.07.25
 * Time: 15:50:31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/management")
public class UserManagementController {
    private final UserManagementUseCase userManagementService;

    @GetMapping("get/id")
    Mono<Result<UserResponse>> getUserById(UUID id) {
        return userManagementService.getUserById(id);
    }

    @GetMapping("/get/username")
    Mono<Result<UserResponse>> getUserByUsername(String username) {
        return userManagementService.getUserByUsername(username);
    }

    @GetMapping("/get/email")
    Mono<Result<UserResponse>> getUserByEmail(String email) {
        return userManagementService.getUserByEmail(email);
    }
}