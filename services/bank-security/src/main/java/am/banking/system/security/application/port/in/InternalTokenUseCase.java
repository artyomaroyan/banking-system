package am.banking.system.security.application.port.in;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 04.07.25
 * Time: 14:13:19
 */
public interface InternalTokenUseCase {
    Mono<ResponseEntity<String>> generateToken(ServerHttpRequest request);
}