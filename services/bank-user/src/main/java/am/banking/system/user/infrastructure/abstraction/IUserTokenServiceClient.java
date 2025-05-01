package am.banking.system.user.infrastructure.abstraction;

import am.banking.system.common.dto.TokenResponse;
import am.banking.system.common.dto.UserDto;
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