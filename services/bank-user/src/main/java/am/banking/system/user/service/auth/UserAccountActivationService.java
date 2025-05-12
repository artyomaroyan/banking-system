package am.banking.system.user.service.auth;

import am.banking.system.user.exception.InvalidUserTokenException;
import am.banking.system.user.exception.UserAccountActivationException;
import am.banking.system.user.infrastructure.security.abstraction.IUserTokenServiceClient;
import am.banking.system.user.model.result.Result;
import am.banking.system.user.service.core.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 20:30:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountActivationService {
    private final UserService userService;
    private final IUserTokenServiceClient userTokenServiceClient;

    public Result<String> activateAccount(String activationToken, String username) {
        validateActivationToken(activationToken, username);
        activateAccountAndDeactivateToken(activationToken, username);
        return Result.success("Your account has been activated");
    }

    private void validateActivationToken(String activationToken, String username) {
        try {
            if (!userTokenServiceClient.validateEmailVerificationToken(activationToken, username)) {
                throw new InvalidUserTokenException("Invalid activation token for user " + username);
            }
        } catch (Exception e) {
            log.error("Token validation failed for user: {}", username, e);
            throw new InvalidUserTokenException("Invalid activation token", e.getCause());
        }
    }

    private void activateAccountAndDeactivateToken(String activationToken, String username) {
        var userResult = userService.getUserByUsername(username);
        if (!userResult.success() || userResult.data() == null) {
            throw new UserAccountActivationException("User not found for username: " + username);
        }

        try {
            userService.updateUserAccountState(userResult.data().id());
            userTokenServiceClient.invalidateUsedToken(activationToken);
        } catch (UserAccountActivationException ex) {
            throw new UserAccountActivationException("User account activation failed", ex.getCause());
        }
    }
}