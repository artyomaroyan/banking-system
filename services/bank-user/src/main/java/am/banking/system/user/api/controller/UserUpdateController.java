package am.banking.system.user.api.controller;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.application.port.in.user.UserUpdateUseCase;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

/**
 * Author: Artyom Aroyan
 * Date: 17.08.25
 * Time: 01:08:53
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserUpdateController {
    private final UserUpdateUseCase userUpdateService;

    @PutMapping("/update/{userId}")
    Mono<ResponseEntity<Result<String>>> update
            (@PathVariable("userId") UUID userId, @RequestBody @Valid UserRequest request) {
        return userUpdateService.update(userId,request)
                .map(this::buildResponse);
    }

    private <T> ResponseEntity<Result<T>> buildResponse(Result<T> result) {
        if (!result.success()) {
            return new ResponseEntity<>(Result.error("action failed", BAD_REQUEST.value()), BAD_REQUEST);
        }
        return new ResponseEntity<>(result, OK);
    }
}