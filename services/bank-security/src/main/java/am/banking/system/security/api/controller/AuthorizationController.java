package am.banking.system.security.api.controller;

import am.banking.system.common.shared.dto.security.AuthorizationRequest;
import am.banking.system.security.application.port.in.AuthorizationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.07.25
 * Time: 15:56:53
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/security")
public class AuthorizationController {
    private final AuthorizationUseCase authorizationService;

    @PostMapping("/authorize")
    public Mono<ResponseEntity<Boolean>> authorize(@Valid @RequestBody AuthorizationRequest request) {
        return authorizationService.isAuthorized(request.token(), request.permission())
                .map(ResponseEntity::ok);
    }
}