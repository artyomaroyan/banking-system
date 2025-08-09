package am.banking.system.security.api.controller;

import am.banking.system.common.shared.dto.security.AuthenticationRequest;
import am.banking.system.common.shared.dto.security.AuthorizationRequest;
import am.banking.system.security.application.port.in.AuthenticationUseCase;
import am.banking.system.security.application.port.in.AuthorizationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
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
public class AuthController {
    private final AuthorizationUseCase authorizationService;
    private final AuthenticationUseCase authenticationService;

    @PostMapping("/authorize")
    public Mono<ResponseEntity<Boolean>> authorize(@Valid @RequestBody AuthorizationRequest request) {
        return authorizationService.isAuthorized(request.token(), request.permission())
                .map(ResponseEntity::ok)
                .onErrorResume(err -> {
                    log.warn("Authorization check failed: {}", err.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(false));
                });
    }

    @PostMapping("/authenticate")
    public Mono<ResponseEntity<Authentication>> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request)
                .map(ResponseEntity::ok)
                .onErrorResume(BadCredentialsException.class, _ ->
                        Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }
}