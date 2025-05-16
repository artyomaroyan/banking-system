package am.banking.system.user.infrastructure.security.abstraction;

import am.banking.system.common.dto.UserDto;
import am.banking.system.common.dto.security.TokenResponse;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:25:23
 */
public interface IJwtTokenServiceClient {
    Mono<TokenResponse> generateJwtToken(@Valid UserDto user);
    Mono<String> generateSystemToken();
    Mono<Boolean> validateJwtToken(String token, String username);
}