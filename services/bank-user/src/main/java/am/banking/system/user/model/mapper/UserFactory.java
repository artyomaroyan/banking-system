package am.banking.system.user.model.mapper;

import am.banking.system.user.infrastructure.security.client.PasswordServiceClient;
import am.banking.system.user.model.dto.UserRequest;
import am.banking.system.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static am.banking.system.common.enums.AccountState.PENDING;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 01:27:15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserFactory {
    private final RoleMapper roleMapper;
    private final PasswordServiceClient securityServiceClient;

    public Mono<User> createUser(UserRequest request) {
        return Mono.zip(securityServiceClient.hashPassword(request.password())
                        .doOnNext(pwd -> log.info("Hashed password: {}", pwd)),
                        roleMapper.getDefaultRole())
                .switchIfEmpty(Mono.error(new RuntimeException("Default role not found")))
                .doOnNext(role -> log.info("Default role: {}", role))
                .map(tuple -> {
                    String password = tuple.getT1();
                    return new User(
                            request.username(),
                            request.firstName(),
                            request.lastName(),
                            request.email(),
                            password,
                            request.phone(),
                            request.age(),
                            PENDING
                    );
                });
    }
}