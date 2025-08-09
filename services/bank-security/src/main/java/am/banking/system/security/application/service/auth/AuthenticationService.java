package am.banking.system.security.application.service.auth;

import am.banking.system.common.shared.dto.security.AuthenticationRequest;
import am.banking.system.security.application.port.in.AuthenticationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 08.08.25
 * Time: 23:00:59
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {
    private final ReactiveAuthenticationManager authenticationManager;

    @Override
    public Mono<Authentication> authenticate(AuthenticationRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        )
                .doOnNext(_ -> log.info("User '{}' authenticated successfully", request.username()))
                .doOnError(error -> log.error("Authentication failed for '{}' : {}", request.username(), error.getMessage()))
                .onErrorResume(err -> Mono.error(new BadCredentialsException("Invalid username or password", err)));
    }
}