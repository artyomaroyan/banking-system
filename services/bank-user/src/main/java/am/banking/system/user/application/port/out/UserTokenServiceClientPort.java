package am.banking.system.user.application.port.out;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.security.TokenValidatorResponse;
import am.banking.system.common.shared.dto.user.UserDto;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:27:57
 */
public interface UserTokenServiceClientPort {
    Mono<TokenResponse> generateEmailVerificationToken(@Valid UserDto user);
    Mono<TokenValidatorResponse> validateEmailVerificationToken(String token, String username);
    Mono<TokenResponse> generatePasswordRecoveryToken(@Valid UserDto user);
    Mono<TokenValidatorResponse> validatePasswordRecoveryToken(String token, String username);
}