package am.banking.system.user.application.service.user;

import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.UserResponse;
import am.banking.system.user.application.port.in.user.UserUpdateUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 10.08.25
 * Time: 22:42:07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserUpdateService implements UserUpdateUseCase {

    @Override
    public Mono<Result<UserResponse>> update(UUID userId) {
        return null;
    }
}