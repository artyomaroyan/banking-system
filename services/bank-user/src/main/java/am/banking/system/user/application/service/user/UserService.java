package am.banking.system.user.application.service.user;

import am.banking.system.common.shared.dto.security.PasswordHashingResponse;
import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.application.port.in.user.UserUseCase;
import am.banking.system.user.application.port.out.security.PasswordHashingClientPort;
import am.banking.system.user.domain.model.Role;
import am.banking.system.user.domain.model.User;
import am.banking.system.user.domain.model.UserRole;
import am.banking.system.user.infrastructure.adapter.out.persistence.UserRepository;
import am.banking.system.user.infrastructure.adapter.out.persistence.UserRoleRepository;
import am.banking.system.user.application.service.access.RoleService;
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
public class UserService implements UserUseCase {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordHashingClientPort passwordHashingClient;

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