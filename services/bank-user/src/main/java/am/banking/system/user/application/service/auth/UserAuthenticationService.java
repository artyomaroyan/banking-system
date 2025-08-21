package am.banking.system.user.application.service.auth;

import am.banking.system.common.shared.dto.security.AuthenticationRequest;
import am.banking.system.common.shared.dto.security.AuthenticationResponse;
import am.banking.system.common.shared.response.Result;
import am.banking.system.user.application.port.in.user.UserAuthenticationUseCase;
import am.banking.system.user.application.port.out.security.AuthenticationClientPort;
import am.banking.system.user.infrastructure.adapter.out.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Author: Artyom Aroyan
 * Date: 08.08.25
 * Time: 23:48:04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthenticationService implements UserAuthenticationUseCase {
    private final UserRepository userRepository;
    private final AuthenticationClientPort authenticationClient;

    @Override
    public Mono<Result<AuthenticationResponse>> authenticate(AuthenticationRequest request) {
        return userRepository.existsByUsername(request.username())
                .flatMap(exists -> {
                    if (!exists) {
                        log.info("User '{}' does not exist", request.username());
                        return Mono.just(Result.error("User does not exists:", NOT_FOUND.value()));
                    }

                    return authenticationClient.authenticate(request.username(), request.password())
                            .map(authResponse -> Result.success(authResponse, "Successfully authenticated"))
                            .onErrorResume(ex -> {
                                log.warn("Invalid credentials: {}", ex.getMessage());
                                return Mono.just(Result.error("Invalid credentials", BAD_REQUEST.value()));
                            });
                });
    }
}