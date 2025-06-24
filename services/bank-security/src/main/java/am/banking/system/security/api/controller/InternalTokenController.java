package am.banking.system.security.api.controller;

import am.banking.system.common.infrastructure.tls.configuration.InternalSecretProperties;
import am.banking.system.common.shared.exception.security.EmptyTokenException;
import am.banking.system.security.application.port.in.JwtTokenServiceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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
    private final JwtTokenServiceUseCase jwtTokenService;
    private final InternalSecretProperties internalSecretProperties;

    @GetMapping("/system-token")
    public Mono<ResponseEntity<String>> generateSystemToken(ServerHttpRequest request) {
        String secret = request.getHeaders().getFirst("X-Internal-Secret");

        if (!internalSecretProperties.secret().equals(secret)) {
            log.warn("Invalid internal secret from {}", request.getRemoteAddress());
            return Mono.just(ResponseEntity.status(FORBIDDEN).body("Invalid internal secret"));
        }
        return Mono.fromSupplier(jwtTokenService::generateSystemToken)
                .doOnNext(token -> {
                    if (token == null || token.isEmpty() || token.trim().isBlank()) {
                        log.error("Generated empty system token");
                        throw new EmptyTokenException("Generated empty system token");
                    }
                    log.info("Generated internal system token: {}", token);
                })
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error during internal token generation: {}", error.getMessage(), error);
                    return Mono.just(ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Failed to generate internal system token"));
                });
    }
}