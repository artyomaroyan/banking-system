package am.banking.system.user.application.port.out.security;

import am.banking.system.common.shared.dto.security.AuthenticationResponse;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 08.08.25
 * Time: 23:28:28
 */
public interface AuthenticationClientPort {
    Mono<AuthenticationResponse> authenticate(String username, String password);
}