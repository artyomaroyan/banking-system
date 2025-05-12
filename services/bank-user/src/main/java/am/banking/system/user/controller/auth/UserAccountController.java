package am.banking.system.user.controller.auth;

import am.banking.system.user.model.dto.UserRequest;
import am.banking.system.user.model.result.Result;
import am.banking.system.user.service.auth.UserAccountActivationService;
import am.banking.system.user.service.auth.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private final UserRegistrationService userRegistrationService;
    private final UserAccountActivationService userAccountActivationService;

    @PostMapping("/register")
    ResponseEntity<Result<String>> register(@Valid @RequestBody UserRequest request) {
        var result = userRegistrationService.register(request);
        return buildResponse(result);
    }

    @GetMapping("/activate")
    ResponseEntity<Result<String>> activateAccount(@RequestBody String token, @RequestBody String username) {
        var result = userAccountActivationService.activateAccount(token, username);
        return buildResponse(result);
    }

    private <T> ResponseEntity<Result<T>> buildResponse(Result<T> result) {
        if (!result.success()) {
            return new ResponseEntity<>(Result.error("action failed", BAD_REQUEST.value()), BAD_REQUEST);
        }
        return new ResponseEntity<>(result, OK);
    }
}