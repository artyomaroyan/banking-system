package am.banking.system.user.api.controller;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.UserResponse;
import am.banking.system.user.application.port.in.UserManagementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/get/me")
    ResponseEntity<Result<UserResponse>> getCurrentUser() {
        return null;
    }
}