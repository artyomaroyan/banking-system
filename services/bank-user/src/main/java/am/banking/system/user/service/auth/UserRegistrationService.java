package am.banking.system.user.service.auth;

import am.banking.system.common.dto.user.UserDto;
import am.banking.system.common.mapper.GenericMapper;
import am.banking.system.common.reponse.Result;
import am.banking.system.user.infrastructure.security.abstraction.IJwtTokenServiceClient;
import am.banking.system.user.infrastructure.security.abstraction.IUserTokenServiceClient;
import am.banking.system.user.model.dto.UserRequest;
import am.banking.system.user.service.notification.EmailSendingService;
import am.banking.system.user.service.user.factory.UserFactory;
import am.banking.system.user.service.validation.RequestValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 00:07:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserFactory userFactory;
    private final GenericMapper genericMapper;
    private final RequestValidation requestValidation;
    private final EmailSendingService emailSendingService;
    private final IJwtTokenServiceClient jwtTokenServiceClient;
    private final IUserTokenServiceClient userTokenServiceClient;

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
                            .doOnNext(user -> log.info("User created: {}", user))
                            .flatMap(savedUser -> {
                                var userDto = genericMapper.map(savedUser, UserDto.class);
                                log.info("Mapped user to DTO: {}", userDto);

                                return userTokenServiceClient.generateEmailVerificationToken(userDto)
                                        .doOnNext(token -> log.info("Generated email verification token: {}", token.token()))
                                        .flatMap(token -> emailSendingService
                                                .sendVerificationEmail(
                                                        userDto.email(),
                                                        userDto.username(),
                                                        token.token()
                                                )
                                                .doOnSuccess(_ -> log.info("Verification email sent to: {}", userDto.email()))
                                                .then(jwtTokenServiceClient.generateJwtToken(userDto))
                                                .doOnNext(jwt -> log.info("Generated jwt token: {}", jwt))
                                        )
                                        .thenReturn(Result.success("Your account has been registered. Please activate it by clicking the activation link we have sent to your email."));
                            })
                            .doOnError(error -> log.error("Registration failed: {}", error.getMessage(), error));
                });
    }
}