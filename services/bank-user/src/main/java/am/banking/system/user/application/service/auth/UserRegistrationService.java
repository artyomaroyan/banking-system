package am.banking.system.user.application.service.auth;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.application.factory.UserFactory;
import am.banking.system.user.application.mapper.UserDtoMapper;
import am.banking.system.user.application.port.in.UserRegistrationUseCase;
import am.banking.system.user.application.port.out.JwtTokenServiceClientPort;
import am.banking.system.user.application.port.out.UserTokenServiceClientPort;
import am.banking.system.user.application.service.validation.RequestValidation;
import am.banking.system.user.infrastructure.adapter.out.notification.NotificationServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:07:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService implements UserRegistrationUseCase {
    private final UserFactory userFactory;
    private final UserDtoMapper userReactiveMapper;
    private final RequestValidation requestValidation;
    private final JwtTokenServiceClientPort jwtTokenServiceClient;
    private final UserTokenServiceClientPort userTokenServiceClient;
    private final NotificationServiceClient notificationServiceClient;

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
                            .doOnNext(user -> log.info("Created user: {}", user))
                            .flatMap(user -> userReactiveMapper.map(user)
                                    .doOnNext(dto -> log.info("Mapped User to DTO: {}", dto))
                                    .doOnNext(dto -> log.info("About to generate verification token for: {}", dto.email()))
                                    .zipWhen(this::generateVerificationToken)
                                    .flatMap(this::sendVerificationEmailAndGenerateJwt)
                                    .onErrorResume(error -> {
                                        log.error("Error during token generation or email sending: {}", error.getMessage(), error);
                                        return Mono.just(Result.error(
                                                "Registration process failed. Please try again later.", INTERNAL_SERVER_ERROR.value()));
                                    })
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

        return notificationServiceClient.sendVerificationEmail(userDto.email(), userDto.username(), token.token())
                .doOnSuccess(_ -> log.info("Verification email sent to: {}", userDto.email()))
                .then(jwtTokenServiceClient.generateJwtToken(userDto))
//                .doOnNext(jwt -> log.info("Generated JSON Web Token: {}", jwt))
                .thenReturn(Result.successMessage(
                        "Your account has been registered. Please activate it by clicking the activation link we have sent to your email."));
    }
}