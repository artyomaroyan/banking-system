package am.banking.system.user.application.service.user;

import am.banking.system.common.shared.exception.user.UserAccountActivationException;
import am.banking.system.common.shared.response.Result;
import am.banking.system.common.util.GenericMapper;
import am.banking.system.user.api.dto.UserResponse;
import am.banking.system.user.application.port.in.user.UserManagementUseCase;
import am.banking.system.user.domain.entity.User;
import am.banking.system.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static am.banking.system.user.util.LogConstants.SUCCESS;
import static am.banking.system.user.util.LogConstants.USER_NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * Author: Artyom Aroyan
 * Date: 12.05.25
 * Time: 21:33:33
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementService implements UserManagementUseCase {
    private final GenericMapper genericMapper;
    private final UserRepository userRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Mono<Void> updateUserAccountState(Integer id) {
        return r2dbcEntityTemplate.update(User.class)
                .matching(Query.query(
                        Criteria.where("id").is(id)
                                .and("account_state").is("PENDING")
                ))
                .apply(Update.update("account_state", "ACTIVE"))
                .flatMap(rows -> {
                    if (rows == 0) {
                        return Mono.error(new UserAccountActivationException(
                                "User account state update failed. No pending user found with id: " + id));
                    }
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Result<UserResponse>> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .map(user -> genericMapper.map(user, UserResponse.class))
                .map(response -> Result.success(response, SUCCESS))
                .defaultIfEmpty(Result.error(USER_NOT_FOUND, NO_CONTENT.value()));
    }

    @Override
    public Mono<Result<UserResponse>> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .map(user -> genericMapper.map(user, UserResponse.class))
                .map(response -> Result.success(response, SUCCESS))
                .defaultIfEmpty(Result.error(USER_NOT_FOUND, NO_CONTENT.value()));
    }

    public Mono<Result<UserResponse>> getCurrentUser(Authentication authentication) {
        return null;
    }
}