package am.banking.system.user.infrastructure.security.abstraction;

import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:21:22
 */
public interface IPasswordServiceClient {
    Mono<String> hashPassword(String password);
    Mono<Boolean> validatePassword(String rawPassword, String hashedPassword);
}