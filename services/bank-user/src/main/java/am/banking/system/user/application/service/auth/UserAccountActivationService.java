package am.banking.system.user.application.service.auth;

import am.banking.system.common.shared.dto.security.TokenValidatorResponse;
import am.banking.system.common.shared.response.Result;
import am.banking.system.user.application.port.in.user.UserAccountActivationUseCase;
import am.banking.system.user.application.port.in.user.UserManagementUseCase;
import am.banking.system.user.application.port.out.TokenInvalidateClientPort;
import am.banking.system.user.application.port.out.UserTokenClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 20:30:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountActivationService implements UserAccountActivationUseCase {
    private final UserTokenClientPort userTokenClient;
    private final UserManagementUseCase userManagementService;
    private final TokenInvalidateClientPort tokenInvalidateClient;

    @Override
    public Mono<Result<String>> activateAccount(Integer userId, String username, String activationToken) {
        return userTokenClient.validateEmailVerificationToken(userId, username, activationToken)
                .flatMap(valid -> {
                    if (!TokenValidatorResponse.valid().success()) {
                        return Mono.just(Result.error("Email validation token validation failed", BAD_REQUEST.value()));
                    }
                    return handleUserActivation(username, activationToken);
                });
    }

    private Mono<Result<String>> handleUserActivation(String username, String activationToken) {
        return userManagementService.getUserByUsername(username)
                .flatMap(userResult -> {
                    if (!userResult.success() ||  userResult.data() == null) {
                        return Mono.just(Result.error("User wit username %s does not found", NOT_FOUND.value()));
                    }

                    return userManagementService.updateUserAccountState(userResult.data().id())
                            .then(tokenInvalidateClient.invalidateUsedToken(activationToken))
                            .thenReturn(Result.success("Your account has been successfully activated"));
                });
    }
}