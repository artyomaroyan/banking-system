package am.banking.system.user.application.port.out;

import am.banking.system.common.shared.dto.security.PasswordHashingResponse;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:21:22
 */
public interface PasswordHashingClientPort {
    Mono<PasswordHashingResponse> hashPassword(String password);
    Mono<Boolean> validatePassword(String rawPassword, String hashedPassword);
}