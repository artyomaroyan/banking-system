package am.banking.system.user.application.service.auth;

import am.banking.system.common.shared.dto.account.AccountCreationRequest;
import am.banking.system.common.shared.dto.account.AccountResponse;
import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.common.shared.response.Result;
import am.banking.system.user.api.dto.UserRequest;
import am.banking.system.user.application.factory.UserFactory;
import am.banking.system.user.application.mapper.ReactiveMapper;
import am.banking.system.user.application.port.in.user.UserRegistrationUseCase;
import am.banking.system.user.application.port.out.NotificationClientPort;
import am.banking.system.user.application.port.out.UserTokenClientPort;
import am.banking.system.user.application.port.out.account.CurrentAccountCreationClientPort;
import am.banking.system.user.application.service.validation.RequestValidation;
import am.banking.system.user.domain.entity.User;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static am.banking.system.common.shared.enums.Currency.AMD;
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
    private final UserTokenClientPort userTokenClient;
    private final NotificationClientPort notificationClient;
    private final ReactiveMapper<User, UserDto> userReactiveMapper;
    private final RequestValidation<UserRequest> requestValidation;
    private final CurrentAccountCreationClientPort currentAccountCreationClient;

//    @Override
//    public Mono<Result<String>> register(UserRequest request) {
//        log.info("Initiating user registration for email: {}", request.email());
//
//        return requestValidation.isValidRequest(request)
//                .flatMap(errors -> {
//                    if (!errors.message().isEmpty()) {
//                        String errorMessage = String.join(" ", errors.message());
//                        log.error("Validation failed for {}: {}",  request.email(), errorMessage);
//                        return Mono.just(Result.error(errorMessage, BAD_REQUEST.value()));
//                    }
//                    log.info("Validation passed for email: {}",  request.email());
//
//                    return userFactory.createUser(request)
//                            .doOnNext(user -> log.info("Created user: {}", user))
//                            .flatMap(userReactiveMapper::map)
//                            .flatMap(user -> createDefaultAccount(user)
//                                    .then(generateVerificationToken(user)
//                                            .flatMap(token ->
//                                                    sendVerificationEmailAndGenerateAccessToken(user, token))
//                                    )
//                            )
//                            .onErrorResume(error -> {
//                                log.error("Registration failed: {}", error.getMessage(), error);
//                                return Mono.just(Result.error("Registration process failed. Please try again later.", INTERNAL_SERVER_ERROR.value()));
//                            });
//                });
//    }

    @Override
    public Mono<Result<String>> register(UserRequest request) {
        return Mono.just(request)
                .doOnNext(req -> log.info("Initiating registration for email: {}", req.email()))
                .flatMap(this::validateRequest)
                .flatMap(this::createUserAndAccount)
                .flatMap(this::processVerification)
                .onErrorResume(this::handleRegistrationError);
    }

    private Mono<UserRequest> validateRequest(UserRequest request) {
        return requestValidation.isValidRequest(request)
                .flatMap(errors -> {
                    if (!errors.message().isEmpty()) {
                        String errorMessage = String.join(" ", errors.message());
                        log.error("Validation failed for {}: {}",  request.email(), errorMessage);
                        return Mono.error(new ValidationException(errorMessage));
                    }
                    return Mono.just(request);
                });
    }

    private Mono<UserDto> createUserAndAccount(UserRequest request) {
        return userFactory.createUser(request)
                .flatMap(userReactiveMapper::map)
                .flatMap(userDto -> createDefaultAccount(userDto)
                        .thenReturn(userDto));
    }

    private Mono<AccountResponse> createDefaultAccount(UserDto userDto) {
        AccountCreationRequest accRequest = new AccountCreationRequest(userDto.userId(), userDto.username(), AMD); // since this is default account which is created by user registration I set account suffix AMD.
        return currentAccountCreationClient.createDefaultAccount(accRequest)
                .doOnNext(account -> log.info("Account created successfully: {}", account));
    }

    private Mono<Result<String>> processVerification(UserDto userDto) {
        return generateVerificationToken(userDto)
                .flatMap(token -> sendVerificationEmail(userDto, token))
                .thenReturn(Result.success("Your account has been registered. Please activate it by clicking the activation link we have sent to your email."));
    }

//    private Mono<Result<String>> sendVerificationEmailAndGenerateAccessToken(UserDto userDto, TokenResponse token) {
//        return notificationClient.sendVerificationEmail(userDto.email(), userDto.username(), token.token())
//                .doOnSuccess(_ -> log.info("Verification email sent to: {}", userDto.email()))
//                .then(userTokenClient.generateJwtAccessToken(userDto))
//                .thenReturn(Result.success("Your account has been registered. Please activate it by clicking the activation link we have sent to your email."));
//    }

    private Mono<TokenResponse> generateVerificationToken(UserDto userDto) {
        return userTokenClient.generateEmailVerificationToken(userDto)
                .doOnNext(token -> log.info("Generated email verification token: {}", token.token().substring(7, 12)));
    }

    private Mono<Void> sendVerificationEmail(UserDto userDto, TokenResponse token) {
        return notificationClient.sendVerificationEmail(userDto.email(), userDto.username(), token.token())
                .doOnSuccess(_ -> log.info("Verification email sent to: {}", userDto.email()));
    }

    private Mono<Result<String>> handleRegistrationError(Throwable error) {
        log.error("Registration failed: {}", error.getMessage(), error);
        if (error instanceof ValidationException) {
            return Mono.just(Result.error(error.getMessage(), BAD_REQUEST.value()));
        }
        return Mono.just(Result.error("Registration process failed. Please try again later.", INTERNAL_SERVER_ERROR.value()));
    }
}