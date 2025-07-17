package am.banking.system.security.api.controller;

import am.banking.system.common.shared.dto.security.TokenInvalidateRequest;
import am.banking.system.security.application.port.in.InvalidateTokenUseCase;
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
 * Time: 22:57:30
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/security/token")
public class TokenInvalidateController {
    private final InvalidateTokenUseCase invalidateTokenService;

    @PostMapping("/invalidate")
    public Mono<ResponseEntity<String>> invalidate(@Valid @RequestBody TokenInvalidateRequest request) {
        return invalidateTokenService.markTokenAsVerified(request.token())
                .thenReturn(ResponseEntity.ok("Token has been marked as verified"));
    }
}