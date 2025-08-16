package am.banking.system.user.application.factory;

import am.banking.system.common.shared.dto.security.PasswordHashingResponse;
import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.application.port.in.user.UserFactoryUseCase;
import am.banking.system.user.application.port.out.PasswordHashingClientPort;
import am.banking.system.user.domain.entity.Role;
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
    private final PasswordHashingClientPort passwordHashingClient;

//    @Override
//    public Mono<User> createUser(UserRequest request) {
//        log.info("Creating user with username: {}", request.username());
//        return Mono.zip(
//                        passwordHashingClient.hashPassword(request.password())
//                                .doOnNext(pwd -> log.info("Hashed rawPassword: {}", pwd))
//                                .doOnError(err -> log.error("Error hashing rawPassword: {}", err.getMessage(), err))
//                                .log("HASH_PASSWORD"),
//                        roleService.getDefaultRole()
//                                .map(set -> set.iterator().next())
//                                .doOnNext(role -> log.info("Retrieved default role: {}", role.getRoleName()))
//                                .doOnError(err -> log.error("Error fetching default role: {}", err.getMessage(), err))
//                                .log("GET_ROLE")
//                )
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
//                                        .doOnNext(_ -> log.info(
//                                                "Linked user '{}' to role '{}'", savedUser.getUsername(), defaultRole.getRoleName()))
//                                        .thenReturn(savedUser);
//                            });
//                })
//                .doOnError(error -> log.error("Error during user creation: {}", error.getMessage(), error));
//    }
//
//    @Override
//    public Mono<User> updateUser(UUID userId, UserRequest request) {
//        User userToUpdate = new User(
//                request.username(),
//                request.firstName(),
//                request.lastName(),
//                request.email(),
//                request.password(),
//                request.phone(),
//                request.age(),
//                ACTIVE
//        );
//        return userRepository.save(userToUpdate);
//    }

    @Override
    public Mono<User> createUser(UserRequest request) {
        return Mono.zip(
                hashPassword(request.password()),
                getDefaultRole()
        )
                .flatMap(tuple -> {
                    var hashedPassword = tuple.getT1();
                    var defaultRole = tuple.getT2();

                    User user = buildUser(request, hashedPassword);
                    return saveUserWithRole(user, defaultRole);
                })
                .doOnError(error -> log.error("Failed to create user: {}", error.getMessage(), error));
    }

    @Override
    public Mono<User> updateUser(User existingUser, UserRequest request) {
        return hashPassword(request.password())
                .flatMap(hashedPassword -> {
                    User updatedUser = buildUser(request, hashedPassword);
                    updatedUser.setId(existingUser.getId());
                    return userRepository.save(updatedUser);
                });
    }

    private Mono<String> hashPassword(String rawPassword) {
        return passwordHashingClient.hashPassword(rawPassword)
                .map(PasswordHashingResponse::hashedPassword)
                .doOnNext(_ -> log.info("Password hashed successfully:"))
                .doOnError(err -> log.error("Password hashing failed: {}", err.getMessage(), err));
    }

    private User buildUser(UserRequest request, String hashedPassword) {
        return User.builder()
                .username(request.username())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .password(hashedPassword)
                .email(request.email())
                .phone(request.phone())
                .age(request.age())
                .accountState(PENDING)
                .build();
    }

    private Mono<Role> getDefaultRole() {
        return roleService.getDefaultRole()
                .map(set -> set.iterator().next())
                .doOnNext(role -> log.info("Retrieved default role: {}", role.getRoleName()));
    }

    private Mono<User> saveUserWithRole(User user, Role role) {
        return userRepository.save(user)
                .flatMap(saved -> assignRole(saved, role).thenReturn(saved));
    }

    private Mono<UserRole> assignRole(User user, Role role) {
        UserRole userRole = new UserRole(user.getId(), role.getId());
        return userRoleRepository.save(userRole)
                .doOnNext(_ -> log.info("Assigned role: {} to user: {}", role.getRoleName(), user.getUsername()));
    }
}