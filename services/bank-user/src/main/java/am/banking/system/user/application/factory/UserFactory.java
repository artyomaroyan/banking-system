package am.banking.system.user.application.factory;

import am.banking.system.user.application.port.in.UserFactoryUseCase;
import am.banking.system.user.infrastructure.adapter.out.security.PasswordServiceClient;
import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.domain.entity.User;
import am.banking.system.user.domain.entity.UserRole;
import am.banking.system.user.domain.repository.UserRepository;
import am.banking.system.user.domain.repository.UserRoleRepository;
import am.banking.system.user.domain.service.access.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static am.banking.system.common.shared.enums.AccountState.PENDING;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 01:27:15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserFactory implements UserFactoryUseCase {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordServiceClient securityServiceClient;

    @Override
    public Mono<User> createUser(UserRequest request) {
        log.info("Creating user with username: {}", request.username());

        return roleService.getDefaultRole()
                .map(set -> set.iterator().next())
                .doOnNext(role -> log.info("Retrieved default role: {}", role))
                .doOnError(err -> log.error("Error fetching default role: {}", err.getMessage(), err))
                .log("GET_ROLE")
                .flatMap(defaultRole -> {

                    User user = new User(
                            request.username(),
                            request.firstName(),
                            request.lastName(),
                            request.email(),
                            request.password(),
                            request.phone(),
                            request.age(),
                            PENDING
                    );

                    log.info("Saving user: {}", user.getUsername());
                    return userRepository.save(user)
                            .doOnNext(saved -> log.info("User saved with ID: {}", saved.getId()))
                            .flatMap(savedUser -> {
                                UserRole userRole = new UserRole(savedUser.getId(), defaultRole.getId());
                                return userRoleRepository.save(userRole)
                                        .doOnNext(_ -> log.info("Linked user '{}' to role '{}'",
                                                savedUser.getUsername(), defaultRole.getRoleName()))
                                        .thenReturn(savedUser);
                            });
                })
                .doOnError(error -> log.error("Error during user creation: {}", error.getMessage(), error));
    }

//    public Mono<User> createUser(UserRequest request) {
//        log.info("Creating user with username: {}", request.username());
//        return Mono.zip(
//                securityServiceClient.hashPassword(request.password())
//                        .doOnNext(pwd -> log.info("Hashed password: {}", pwd))
//                        .doOnError(err -> log.error("Error hashing password: {}", err.getMessage(), err))
//                        .log("HASH_PASSWORD"),
//                roleService.getDefaultRole()
//                        .map(set -> set.iterator().next())
//                        .doOnNext(role -> log.info("Retrieved default role: {}", role))
//                        .doOnError(err -> log.error("Error fetching default role: {}", err.getMessage(), err))
//                        .log("GET_ROLE")
//        )
//                .flatMap(tuple -> {
//                    String hashedPassword = tuple.getT1().hashedPassword();
//                    Role defaultRole = tuple.getT2();
//
//                    User user = new User(
//                            request.username(),
//                            request.firstName(),
//                            request.lastName(),
//                            request.email(),
//                            hashedPassword,
//                            request.phone(),
//                            request.age(),
//                            PENDING
//                    );
//
//                    log.info("Saving user: {}", user.getUsername());
//                    return userRepository.save(user)
//                            .doOnNext(saved -> log.info("User saved with ID: {}", saved.getId()))
//                            .flatMap(savedUser -> {
//                                UserRole userRole =  new UserRole(savedUser.getId(), defaultRole.getId());
//                                return userRoleRepository.save(userRole)
//                                        .doOnNext(_ -> log.info("Linked user '{}' to role '{}'", savedUser.getUsername(), defaultRole.getRoleName()))
//                                        .thenReturn(savedUser);
//                            });
//                })
//                .doOnError(error -> log.error("Error during user creation: {}", error.getMessage(), error));
//    }
}