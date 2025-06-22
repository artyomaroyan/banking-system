package am.banking.system.user.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 22.06.25
 * Time: 23:45:08
 */
@Slf4j
@RestController
public class UserFallbackController {

    @RequestMapping("/**")
    public Mono<ResponseEntity<String>> fallback(ServerHttpRequest request) {
        log.warn("Fallback hit for path: {}, URI path: {}", request.getPath(),  request.getURI().getPath());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("No handler found"));
    }
}