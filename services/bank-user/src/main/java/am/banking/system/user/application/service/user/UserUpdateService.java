package am.banking.system.user.application.service.user;

import am.banking.system.common.shared.exception.NotFoundException;
import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.api.dto.UserResponse;
import am.banking.system.user.application.port.in.user.UserFactoryUseCase;
import am.banking.system.user.application.port.in.user.UserUpdateUseCase;
import am.banking.system.user.application.service.validation.RequestValidation;
import am.banking.system.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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
    private final UserFactoryUseCase userFactory;
    private final RequestValidation<UserRequest> requestValidation;

    @Override
    public Mono<Result<UserResponse>> update(UUID userId, UserRequest request) {
        var userFromDb = userRepository.findById(userId).block();

        return userRepository.findById(userFromDb.getId())
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No user found with id {}", userId);
                    return Mono.defer(() -> new NotFoundException("No user found with id " + userId));
                }));

        return requestValidation.isValidRequest(request)
                .flatMap(errors -> {
                    if (!errors.message().isEmpty()) {
                        String errorMessage = String.join(", ", errors.message());
                        log.warn("Invalid request received: {}", errorMessage);
                        return Mono.just(Result.error(errorMessage, BAD_REQUEST.value()));
                    }

                    return userFactory.updateUser(userId, request)
                            .doOnNext(user -> log.info("user updated: {}", user))
                            .onErrorResume(error -> {
                                log.error("Could not update user: {}", error.getMessage(), error);
                                return Mono.just(Result.error("Update failed. please try again later", INTERNAL_SERVER_ERROR.value()));
                            });
                });
    }
}