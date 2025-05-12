package am.banking.system.user.model.mapper;

import am.banking.system.common.enums.AccountState;
import am.banking.system.user.infrastructure.security.client.PasswordServiceClient;
import am.banking.system.user.model.dto.UserRequest;
import am.banking.system.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 01:27:15
 */
@Component
@RequiredArgsConstructor
public class UserFactory {
    private final RoleMapper roleMapper;
    private final PasswordServiceClient securityServiceClient;

    protected User createUser(UserRequest request) {
        return new User(
                request.username(),
                request.firstName(),
                request.lastName(),
                request.email(),
                securityServiceClient.hashPassword(request.password()),
                request.phone(),
                request.age(),
                AccountState.PENDING,
                roleMapper.getDefaultRole()
        );
    }
}