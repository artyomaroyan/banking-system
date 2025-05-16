package am.banking.system.user.infrastructure.security.abstraction;

import am.banking.system.common.dto.UserDto;
import am.banking.system.common.dto.security.TokenResponse;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:27:57
 */
public interface IUserTokenServiceClient {
    Mono<TokenResponse> generateEmailVerificationToken(@Valid UserDto user);
    Mono<Boolean> validateEmailVerificationToken(String token, String username);
    Mono<TokenResponse> generatePasswordRecoveryToken(@Valid UserDto user);
    Mono<Boolean> validatePasswordRecoveryToken(String token, String username);
    Mono<Void> invalidateUsedToken(String token);
}