package am.banking.system.security.application.port.in;

import am.banking.system.security.api.shared.UserPrincipal;
/**
 * Author: Artyom Aroyan
 * Date: 23.04.25
 * Time: 00:00:48
 */
public interface JwtTokenServiceUseCase {
    String generateJwtToken(UserPrincipal user);
    String generateSystemToken();
//    Mono<String> generateSystemToken();
}