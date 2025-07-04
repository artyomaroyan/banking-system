package am.banking.system.security.api.controller;

import am.banking.system.security.application.port.in.InternalTokenUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 17.05.25
 * Time: 01:31:11
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/secure/local")
public class InternalTokenController {
    private final InternalTokenUseCase internalTokenService;

    @GetMapping("/system-token")
    public Mono<ResponseEntity<String>> generateSystemToken(ServerHttpRequest request) {
        return internalTokenService.generateToken(request)
                .filter(response -> response != null && response.getBody() != null && !response.getBody().isBlank())
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("System token response is empty");
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Empty system token"));
                }));
    }
}