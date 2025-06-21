package am.banking.system.user.application.service.auth;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.common.shared.response.Result;
import am.banking.system.user.infrastructure.client.security.IJwtTokenServiceClient;
import am.banking.system.user.infrastructure.client.security.IUserTokenServiceClient;
import am.banking.system.user.mapper.reactive.UserDtoMapper;
import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.application.service.notification.EmailSendingService;
import am.banking.system.user.application.service.user.factory.UserFactory;
import am.banking.system.user.application.service.validation.RequestValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:07:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService implements IUserRegistrationService {
    private final UserFactory userFactory;
    private final UserDtoMapper userReactiveMapper;
    private final RequestValidation requestValidation;
    private final EmailSendingService emailSendingService;
    private final IJwtTokenServiceClient jwtTokenServiceClient;
    private final IUserTokenServiceClient userTokenServiceClient;

    @Override
    public Mono<Result<String>> register(UserRequest request) {
        log.info("Initiating user registration for email: {}", request.email());

        return requestValidation.validateRequest(request)
                .doOnNext(errors -> {
                    if (!errors.isEmpty()) {
                        log.warn("Validation errors occurred during registration: {}", errors);
                    } else {
                        log.info("Validation passed for: {}", request.email());
                    }
                })
                .flatMap(errors -> {
                    if (!errors.isEmpty()) {
                        String message = String.join(" ", errors);
                        return Mono.just(Result.error(message, BAD_REQUEST.value()));
                    }

                    return userFactory.createUser(request)
                            .doOnSubscribe(_ -> log.info("Subscribing to create user process"))
                            .doOnNext(user ->  log.info("Created user: {}", user))
                            .flatMap(user -> userReactiveMapper.map(user)
                                    .doOnNext(dto -> log.info("Mapped User to DTO: {}", dto))
                                    .zipWhen(this::generateVerificationToken)
                                    .flatMap(this::sendVerificationEmailAndGenerateJwt)
                            )
                            .doOnError(error -> log.error("Error while creating user: {}", error.getMessage(), error));
                });
    }

    private Mono<TokenResponse> generateVerificationToken(UserDto userDto) {
        return userTokenServiceClient.generateEmailVerificationToken(userDto)
                .doOnNext(token -> log.info("Generated email verification token: {}", token.token()));
    }

    private Mono<Result<String>> sendVerificationEmailAndGenerateJwt(Tuple2<UserDto, TokenResponse> tuple) {
        UserDto userDto = tuple.getT1();
        TokenResponse token = tuple.getT2();

        return emailSendingService.sendVerificationEmail(userDto.email(), userDto.username(), token.token())
                .doOnSuccess(_ -> log.info("Verification email sent to: {}", userDto.email()))
                .then(jwtTokenServiceClient.generateJwtToken(userDto))
                .doOnNext(jwt -> log.info("Generated JSON Web Token: {}", jwt))
                .thenReturn(Result.successMessage("Your account has been registered. Please activate it by clicking the activation link we have sent to your email."));
    }
}