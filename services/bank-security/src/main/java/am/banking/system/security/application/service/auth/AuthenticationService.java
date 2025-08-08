package am.banking.system.security.application.service.auth;

import am.banking.system.common.shared.dto.security.AuthenticationRequest;
import am.banking.system.security.application.port.in.AuthenticationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 08.08.25
 * Time: 23:00:59
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {
    private final AuthenticationManager authenticationManager;

    @Override
    public Mono<Authentication> authenticate(AuthenticationRequest request) {
        return Mono.just(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())));
    }
}