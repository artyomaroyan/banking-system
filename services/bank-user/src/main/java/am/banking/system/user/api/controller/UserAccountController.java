package am.banking.system.user.api.controller;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.application.service.auth.IUserAccountActivationService;
import am.banking.system.user.application.service.auth.IUserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 23:06:28
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/account")
public class UserAccountController {
    private final IUserRegistrationService userRegistrationService;
    private final IUserAccountActivationService userAccountActivationService;

    @PostMapping("/register")
    Mono<ResponseEntity<Result<String>>> register(@Valid @RequestBody UserRequest request) {
        return userRegistrationService.register(request)
                .map(this::buildResponse);
    }

    @GetMapping("/activate")
    Mono<ResponseEntity<Result<String>>> activateAccount(@RequestParam String token, @RequestParam String username) {
        return userAccountActivationService.activateAccount(token, username)
                        .map(this::buildResponse);
    }

    private <T> ResponseEntity<Result<T>> buildResponse(Result<T> result) {
        if (!result.success()) {
            return new ResponseEntity<>(Result.error("action failed", BAD_REQUEST.value()), BAD_REQUEST);
        }
        return new ResponseEntity<>(result, OK);
    }
}