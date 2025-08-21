package am.banking.system.user.application.service.user;

import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.common.shared.exception.NotFoundException;
import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.api.mapper.ReactiveMapper;
import am.banking.system.user.application.port.in.user.UserUseCase;
import am.banking.system.user.application.port.in.user.UserUpdateUseCase;
import am.banking.system.user.application.service.validation.RequestValidation;
import am.banking.system.user.domain.model.User;
import am.banking.system.user.infrastructure.adapter.out.persistence.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

/**
 * Author: Artyom Aroyan
 * Date: 10.08.25
 * Time: 22:42:07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserUpdateService implements UserUpdateUseCase {
    private final UserRepository userRepository;
    private final UserUseCase userFactory;
    private final ReactiveMapper<User, UserDto> userReactiveMapper;
    private final RequestValidation<UserRequest> requestValidation;

    @Override
    public Mono<Result<String>> update(UUID userId, UserRequest request) {
        return validateRequest(request)
                .flatMap(validRequest -> processUserUpdate(userId, validRequest))
                .onErrorResume(this::handleRegistrationError);
    }

    private Mono<UserRequest> validateRequest(UserRequest request) {
        return requestValidation.isValidRequest(request)
                .flatMap(errors -> {
                    if (!errors.message().isEmpty()) {
                        String errorMessage = String.join(" ", errors.message());
                        log.error("Validation failed for {}: {}",  request.email(), errorMessage);
                        return Mono.error(new ValidationException(errorMessage));
                    }
                    return Mono.just(request);
                });
    }

    private Mono<Result<String>> processUserUpdate(UUID userId, UserRequest request) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id:" + userId)))
                .flatMap(existingUser -> userFactory.updateUser(existingUser, request))
                .flatMap(userReactiveMapper::map)
                .thenReturn(Result.success("Data successfully updated"))
                .onErrorResume(NotFoundException.class, ex -> {
                    log.warn(ex.getMessage());
                    return Mono.just(Result.error(ex.getMessage(), NOT_FOUND.value()));
                })
                .onErrorResume(ex -> {
                    log.warn("Update failed for user {}: {}", userId, ex.getMessage(), ex);
                    return Mono.just(Result.error("Update failed. Please try again later.", INTERNAL_SERVER_ERROR.value()));
                });
    }

    private Mono<Result<String>> handleRegistrationError(Throwable error) {
        log.error("Data update failed: {}", error.getMessage(), error);
        if (error instanceof ValidationException) {
            return Mono.just(Result.error(error.getMessage(), BAD_REQUEST.value()));
        }
        return Mono.just(Result.error("Update process failed. Please try again later.", INTERNAL_SERVER_ERROR.value()));
    }
}