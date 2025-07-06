package am.banking.system.user.application.port.out;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.security.TokenValidatorResponse;
import am.banking.system.common.shared.dto.user.UserDto;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:25:23
 */
public interface JwtTokenServiceClientPort {
    Mono<TokenResponse> generateJwtToken(@Valid UserDto user);
    Mono<String> generateSystemToken();
    Mono<TokenValidatorResponse> validateJwtToken(String token, String username);
}