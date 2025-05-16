package am.banking.system.user.service.core;

import am.banking.system.user.exception.UserAccountActivationException;
import am.banking.system.user.model.dto.UserResponse;
import am.banking.system.user.model.entity.User;
import am.banking.system.user.model.mapper.UserMapper;
import am.banking.system.user.model.repository.UserRepository;
import am.banking.system.user.model.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static am.banking.system.common.enums.AccountState.ACTIVE;
import static am.banking.system.common.enums.AccountState.PENDING;
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
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Transactional
    public Mono<Void> updateUserAccountState(Long id) {
        Query query = new Query(Criteria.where("_id").is(id).and("state").is(PENDING));
        Update update = new Update().set("state", ACTIVE);

        return reactiveMongoTemplate.updateFirst(query,update,User.class)
                .flatMap(result -> {
                    if (result.getModifiedCount() == 0) {
                        return Mono.error(new UserAccountActivationException(
                                "User account state update failed. No pending user found with id: " + id));
                    }
                    return Mono.empty();
                });
    }

    public Mono<Result<UserResponse>> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .map(userMapper::mapFromEntityToResponse)
                .map(response -> Result.success(response, SUCCESS))
                .defaultIfEmpty(Result.error(USER_NOT_FOUND, NO_CONTENT.value()));
    }

    public Mono<Result<UserResponse>> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .map(userMapper::mapFromEntityToResponse)
                .map(response -> Result.success(response, SUCCESS))
                .defaultIfEmpty(Result.error(USER_NOT_FOUND, NO_CONTENT.value()));
    }
}