package am.banking.system.user.application.port.out;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.security.TokenValidatorResponse;
import am.banking.system.common.shared.dto.user.UserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:27:57
 */
public interface UserTokenClientPort {
    Mono<String> generateSystemToken();
    Mono<TokenResponse> generateEmailVerificationToken(@Valid UserDto user);
    Mono<TokenResponse> generateJwtAccessToken(@Valid UserDto user);
    Mono<TokenResponse> generatePasswordRecoveryToken(@Valid UserDto user);
    Mono<Boolean> validateEmailVerificationToken(@NotBlank String token, @NotBlank String username);
    Mono<TokenValidatorResponse> validateJwtAccessToken(@NotBlank String token, @NotBlank String username);
    Mono<TokenValidatorResponse> validatePasswordRecoveryToken(@NotBlank String token, @NotBlank String username);
}