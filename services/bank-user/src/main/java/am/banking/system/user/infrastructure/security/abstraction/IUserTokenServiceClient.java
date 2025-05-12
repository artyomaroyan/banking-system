package am.banking.system.user.infrastructure.security.abstraction;

import am.banking.system.common.dto.UserDto;
import am.banking.system.common.dto.security.TokenResponse;
import jakarta.validation.Valid;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:27:57
 */
public interface IUserTokenServiceClient {
    TokenResponse generateEmailVerificationToken(@Valid UserDto user);
    Boolean validateEmailVerificationToken(String token, String username);
    TokenResponse generatePasswordRecoveryToken(@Valid UserDto user);
    Boolean validatePasswordRecoveryToken(String token, String username);
    String invalidateUsedToken(String token);
}