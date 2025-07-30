package am.banking.system.user.application.factory;

import am.banking.system.common.shared.dto.security.PasswordHashingResponse;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.common.shared.exception.NotFoundException;
import am.banking.system.user.api.dto.PasswordResetConfirmRequest;
import am.banking.system.user.api.dto.PasswordResetEmailRequest;
import am.banking.system.user.application.mapper.ReactiveMapper;
import am.banking.system.user.application.port.in.password.PasswordRecoveryFactoryUserCase;
import am.banking.system.user.application.port.out.NotificationClientPort;
import am.banking.system.user.application.port.out.PasswordHashingClientPort;
import am.banking.system.user.application.port.out.UserTokenClientPort;
import am.banking.system.user.domain.entity.User;
import am.banking.system.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 25.07.25
 * Time: 21:34:52
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordRecoveryFactory implements PasswordRecoveryFactoryUserCase {
    private final UserRepository userRepository;
    private final UserTokenClientPort userTokenClient;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final NotificationClientPort notificationClient;
    private final ReactiveMapper<User, UserDto> userDtoMapper;
    private final PasswordHashingClientPort passwordHashingClient;

    @Override
    public Mono<Void> sendPasswordResetEmail(PasswordResetEmailRequest request) {
        return findUserAndMapToDto(request.userId())
                .flatMap(userDto -> generateResetTokenAndSendEmail(userDto, request))
                .doOnSuccess(_ -> log.info("[sendPasswordResetEmail] Success — userId={}, email={}", request.userId(), request.email()))
                .doOnError(ex -> log.error("[sendPasswordResetEmail] Failed for userId={} — {}", request.email(), ex.getMessage(), ex));
    }

    @Override
    public Mono<Void> completePasswordReset(PasswordResetConfirmRequest request) {
        return findUserAndMapToDto(request.userId())
                .flatMap(userDto -> validateToken(userDto, request.resetToken()))
                .flatMap(_ -> hashNewPassword(request.newPassword())
                        .flatMap(hashedPassword -> updatePassword(request.userId(), hashedPassword)))
                .doOnSuccess(_ -> log.info("[resetPassword] Success for userId={} — {}", request.userId(), request.resetToken()))
                .doOnError(ex -> log.error("[resetPassword] Failed for userId={} — {}", request.userId(), ex.getMessage(), ex));
    }

    private Mono<UserDto> findUserAndMapToDto(Integer userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id: " + userId)))
                .flatMap(userDtoMapper::map);

    }

    private Mono<Void> generateResetTokenAndSendEmail(UserDto userDto, PasswordResetEmailRequest request) {
        return userTokenClient.generatePasswordRecoveryToken(userDto)
                .flatMap(tokenResponse -> notificationClient.sendPasswordResetEmail(
                        request.email(), userDto.username(), tokenResponse.token()));
    }

    private Mono<UserDto> validateToken(UserDto userDto, String token) {
        return userTokenClient.validatePasswordRecoveryToken(
                        userDto.userId(),
                        userDto.username(),
                        token
                )
                .thenReturn(userDto);
    }

    private Mono<String> hashNewPassword(String newPassword) {
        return passwordHashingClient.hashPassword(newPassword)
                .map(PasswordHashingResponse::hashedPassword);
    }

    private Mono<Void> updatePassword(Integer userId, String newPassword) {
        String sql = """
                UPDATE usr.usr
                SET password = :newPassword
                WHERE id = :userId
                """;
        return r2dbcEntityTemplate
                .getDatabaseClient()
                .sql(sql)
                .bind("newPassword", newPassword)
                .bind("userId", userId)
                .fetch()
                .rowsUpdated()
                .flatMap(rows -> rows > 0 ?
                        Mono.empty() :
                        Mono.error(new IllegalStateException("No rows updated for userId: " + userId)));
    }
}