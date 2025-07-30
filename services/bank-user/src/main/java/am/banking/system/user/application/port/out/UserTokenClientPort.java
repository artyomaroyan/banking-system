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
    Mono<TokenResponse> generateJwtAccessToken(@Valid UserDto user);
    Mono<TokenResponse> generateEmailVerificationToken(@Valid UserDto user);
    Mono<TokenResponse> generatePasswordRecoveryToken(@Valid UserDto user);
    Mono<TokenValidatorResponse> validateJwtAccessToken(@NotNull Integer userId, @NotBlank String username, @NotBlank String token);
    Mono<TokenValidatorResponse> validatePasswordRecoveryToken(@NotNull Integer userId, @NotBlank String username, @NotBlank String token);
    Mono<TokenValidatorResponse> validateEmailVerificationToken(@NotNull Integer userId, @NotBlank String username, @NotBlank String token);
}