package am.banking.system.security.application.port.in;

import am.banking.system.common.shared.dto.security.AuthenticationRequest;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 08.08.25
 * Time: 22:59:53
 */
public interface AuthenticationUseCase {
    Mono<Authentication> authenticate(AuthenticationRequest request);
}