package am.banking.system.user.service.user.factory;

import am.banking.system.user.infrastructure.security.client.PasswordServiceClient;
import am.banking.system.user.model.dto.UserRequest;
import am.banking.system.user.model.entity.Role;
import am.banking.system.user.model.entity.User;
import am.banking.system.user.model.entity.UserRole;
import am.banking.system.user.model.repository.UserRepository;
import am.banking.system.user.model.repository.UserRoleRepository;
import am.banking.system.user.service.user.access.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
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
    private final RoleService roleService;
    private final PasswordServiceClient securityServiceClient;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public Mono<User> createUser(UserRequest request) {
        log.info("Creating user with username: {}", request.username());
        return Mono.zip(
                securityServiceClient.hashPassword(request.password())
                        .doOnNext(pwd -> log.info("Hashed password: {}", pwd)),
                roleService.getDefaultRole()
                        .doOnNext(role -> log.info("Retrieved default role: {}", role))
                        .flatMapMany(Flux::fromIterable)
                        .singleOrEmpty()
                        .switchIfEmpty(Mono.error(new RuntimeException("Default role not found")))
        )
                .flatMap(tuple -> {
                    String hashedPassword = tuple.getT1();
                    Role defaultRole = tuple.getT2();

                    User user = new User(
                            request.username(),
                            request.firstName(),
                            request.lastName(),
                            request.email(),
                            hashedPassword,
                            request.phone(),
                            request.age(),
                            PENDING
                    );

                    log.info("Saving user: {}", user.getUsername());
                    return userRepository.save(user)
                            .doOnNext(saved -> log.info("User saved with ID: {}", saved.getId()))
                            .flatMap(savedUser -> {
                                UserRole userRole =  new UserRole(savedUser.getId(), defaultRole.getId());
                                return userRoleRepository.save(userRole)
                                        .doOnNext(_ -> log.info("Linked user '{}' to role '{}'", savedUser.getUsername(), defaultRole.getRoleName()))
                                        .thenReturn(savedUser);
                            });
                })
                .doOnError(error -> log.error("Error during user creation: {}", error.getMessage(), error));
    }
}