package am.banking.system.user.application.port.in.user;

import am.banking.system.common.shared.dto.security.AuthenticationRequest;
import am.banking.system.common.shared.dto.security.AuthenticationResponse;
import am.banking.system.common.shared.response.Result;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 31.07.25
 * Time: 23:46:14
 */
public interface UserAuthenticationUseCase {
    Mono<Result<AuthenticationResponse>> authenticate(AuthenticationRequest request);
}