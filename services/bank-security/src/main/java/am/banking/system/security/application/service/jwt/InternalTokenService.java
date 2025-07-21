package am.banking.system.security.application.service.jwt;

import am.banking.system.common.infrastructure.configuration.InternalSecretProperties;
import am.banking.system.common.shared.exception.security.token.EmptyTokenException;
import am.banking.system.security.application.port.in.InternalTokenUseCase;
import am.banking.system.security.application.port.in.UserTokenUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Author: Artyom Aroyan
 * Date: 04.07.25
 * Time: 14:14:38
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InternalTokenService implements InternalTokenUseCase {
    private final UserTokenUseCase userTokenService;
    private final InternalSecretProperties properties;

    @Override
    public Mono<ResponseEntity<String>> generateToken(ServerHttpRequest request) {
        String secret = request.getHeaders().getFirst("X-Internal-Secret");

        if (!properties.secret().equals(secret)) {
            log.warn("Invalid internal secret from {}", request.getRemoteAddress());
            return Mono.just(ResponseEntity.status(FORBIDDEN).body("Invalid internal secret"));
        }

        return userTokenService.generateSystemToken()
                .flatMap(token -> {
                    if (token == null || token.isEmpty() || token.trim().isBlank()) {
                        log.error("Generated empty system token");
                        return Mono.error(new EmptyTokenException("Generated empty system token"));
                    }
                    log.info("Generated internal system token: {}", token);
                    return Mono.just(ResponseEntity.ok(token));
                })

                .onErrorResume(error -> {
                    log.error("Error during internal token generation: {}", error.getMessage(), error);
                    return Mono.just(ResponseEntity.status(INTERNAL_SERVER_ERROR)
                            .body("Failed to generateInternalToken internal system token"));
                });
    }
}