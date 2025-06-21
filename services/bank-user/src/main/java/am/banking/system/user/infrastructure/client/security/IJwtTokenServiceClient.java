package am.banking.system.user.infrastructure.client.security;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.user.UserDto;
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