package am.banking.system.user.service.user;

import am.banking.system.common.mapper.GenericMapper;
import am.banking.system.common.reponse.Result;
import am.banking.system.user.exception.UserAccountActivationException;
import am.banking.system.user.model.dto.UserResponse;
import am.banking.system.user.model.entity.User;
import am.banking.system.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
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
public class UserService {
    private final GenericMapper genericMapper;
    private final UserRepository userRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public Mono<Void> updateUserAccountState(Long id) {
        return r2dbcEntityTemplate.update(User.class)
                .matching(Query.query(
                        Criteria.where("id").is(id)
                                .and("state").is("PENDING")
                ))
                .apply(Update.update("state", "ACTIVE"))
                .flatMap(rows -> {
                    if (rows == 0) {
                        return Mono.error(new UserAccountActivationException(
                                "User account state update failed. No pending user found with id: " + id));
                    }
                    return Mono.empty();
                });
    }

    public Mono<Result<UserResponse>> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .map(user -> genericMapper.map(user, UserResponse.class))
                .map(response -> Result.success(response, SUCCESS))
                .defaultIfEmpty(Result.error(USER_NOT_FOUND, NO_CONTENT.value()));
    }

    public Mono<Result<UserResponse>> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .map(user -> genericMapper.map(user, UserResponse.class))
                .map(response -> Result.success(response, SUCCESS))
                .defaultIfEmpty(Result.error(USER_NOT_FOUND, NO_CONTENT.value()));
    }
}