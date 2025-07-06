package am.banking.system.security.api.controller;

import am.banking.system.common.shared.dto.security.PasswordHashingRequest;
import am.banking.system.common.shared.dto.security.PasswordHashingResponse;
import am.banking.system.common.shared.dto.security.PasswordValidatorRequest;
import am.banking.system.security.application.port.out.CustomPasswordEncoder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.07.25
 * Time: 14:31:11
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/security/password")
public class PasswordController {
    private final CustomPasswordEncoder passwordEncoder;

    @PostMapping("/hash")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('DO_INTERNAL_TASKS')")
    public Mono<ResponseEntity<PasswordHashingResponse>> hash(@Valid @RequestBody PasswordHashingRequest request) {
        return Mono.fromCallable(() -> passwordEncoder.encode(request.rawPassword()))
                .map(hashed -> ResponseEntity.ok(new PasswordHashingResponse(hashed)));
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('DO_INTERNAL_TASKS')")
    public Mono<ResponseEntity<Boolean>> validate(@Valid @RequestBody PasswordValidatorRequest request) {
        return Mono.fromCallable(() ->
                        passwordEncoder.matches(request.rawPassword(), request.hashedPassword()))
                .map(ResponseEntity::ok);
    }
}