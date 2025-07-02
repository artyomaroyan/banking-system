package am.banking.system.security.api.controller;

import am.banking.system.common.shared.dto.security.AuthorizationRequest;
import am.banking.system.security.application.service.auth.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final AuthorizationService authorizationService;

    @PostMapping("/authorize")
    public ResponseEntity<Boolean> authorize(@Valid @RequestBody AuthorizationRequest request) {
        return ResponseEntity.ok(authorizationService.isAuthorized(request.token(), request.permission()));
    }
}