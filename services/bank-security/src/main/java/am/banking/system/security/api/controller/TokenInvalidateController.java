package am.banking.system.security.api.controller;

import am.banking.system.security.application.port.in.UserTokenServiceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final UserTokenServiceUseCase userTokenService;

    @PostMapping("/invalidate")
    public Mono<ResponseEntity<String>> invalidate() {
        return userTokenService.markTokensForciblyExpired()
                .thenReturn(ResponseEntity.ok("Tokens marked as forcibly expired"));
    }
}