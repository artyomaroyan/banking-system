package am.banking.system.user.api.controller;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.PasswordResetRequest;
import am.banking.system.user.application.port.in.password.PasswordRecoveryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 24.07.25
 * Time: 22:30:31
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password")
public class PasswordRecoveryController {
    private final PasswordRecoveryUseCase passwordRecoveryService;

    @PostMapping("/reset")
    public Mono<Result<String>> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        return passwordRecoveryService.resetPassword(request);
    }
}