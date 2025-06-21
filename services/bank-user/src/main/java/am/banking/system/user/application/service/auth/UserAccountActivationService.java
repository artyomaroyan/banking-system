package am.banking.system.user.application.service.auth;

import am.banking.system.common.shared.response.Result;
import am.banking.system.common.shared.exception.user.InvalidUserTokenException;
import am.banking.system.common.shared.exception.user.UserAccountActivationException;
import am.banking.system.user.application.port.in.ActivateUserAccountUseCase;
import am.banking.system.user.application.port.out.UserTokenServiceClientPort;
import am.banking.system.user.application.port.in.ManageUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 20:30:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountActivationService implements ActivateUserAccountUseCase {
    private final ManageUserUseCase userService;
    private final UserTokenServiceClientPort userTokenServiceClient;

    @Override
    public Mono<Result<String>> activateAccount(String activationToken, String username) {
        return userTokenServiceClient.validateEmailVerificationToken(activationToken, username)
                .flatMap(valid -> {
                    if (Boolean.FALSE.equals(valid)) {
                        log.error("Invalid activation token: {}", activationToken);
                        return Mono.error(new InvalidUserTokenException("Invalid activation token for user: " + username));
                    }

                    return userService.getUserByUsername(username)
                            .flatMap(userResult -> {
                                if (!userResult.success() || userResult.data() == null) {
                                    log.error("User not found: {}", username);
                                    return Mono.error(new UserAccountActivationException("User not found for username: " + username));
                                }

                                return userService.updateUserAccountState(userResult.data().id())
                                        .then(userTokenServiceClient.invalidateUsedToken(activationToken))
                                        .thenReturn(Result.success("Your account has been successfully activated"));
                            });
                });
    }
}