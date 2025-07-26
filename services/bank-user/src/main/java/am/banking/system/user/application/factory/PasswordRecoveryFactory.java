package am.banking.system.user.application.factory;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.common.shared.exception.NotFoundException;
import am.banking.system.user.api.dto.PasswordResetRequest;
import am.banking.system.user.application.mapper.ReactiveMapper;
import am.banking.system.user.application.port.in.password.PasswordRecoveryFactoryUserCase;
import am.banking.system.user.application.port.out.UserTokenClientPort;
import am.banking.system.user.domain.entity.User;
import am.banking.system.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ReactiveMapper<User, UserDto> userDtoMapper;

    @Override
    public Mono<TokenResponse> resetPassword(PasswordResetRequest request) {
        return userRepository.findById(request.userId())
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id: " + request.userId())))
                .flatMap(userDtoMapper::map)
                .flatMap(userTokenClient::generatePasswordRecoveryToken);
    }
}