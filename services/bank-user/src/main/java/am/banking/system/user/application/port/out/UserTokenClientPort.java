package am.banking.system.user.application.port.out;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.security.TokenValidatorResponse;
import am.banking.system.common.shared.dto.user.UserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    Mono<Boolean> validateEmailVerificationToken(@NotNull Integer userId, @NotBlank String token, @NotBlank String username);
    Mono<TokenValidatorResponse> validateJwtAccessToken(@NotNull Integer userId, @NotBlank String token, @NotBlank String username);
    Mono<TokenValidatorResponse> validatePasswordRecoveryToken(@NotNull Integer userId, @NotBlank String token, @NotBlank String username);
}