package am.banking.system.user.infrastructure.security.abstraction;

import am.banking.system.common.dto.security.PasswordHashingResponse;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:21:22
 */
public interface IPasswordServiceClient {
    Mono<PasswordHashingResponse> hashPassword(String password);
    Mono<Boolean> validatePassword(String rawPassword, String hashedPassword);
}