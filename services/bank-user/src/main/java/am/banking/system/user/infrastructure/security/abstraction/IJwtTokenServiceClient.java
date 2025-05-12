package am.banking.system.user.infrastructure.security.abstraction;

import am.banking.system.common.dto.UserDto;
import am.banking.system.common.dto.security.TokenResponse;
import jakarta.validation.Valid;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:25:23
 */
public interface IJwtTokenServiceClient {
    TokenResponse generateJwtToken(@Valid UserDto user);
    Boolean validateJwtToken(String token, String username);
}