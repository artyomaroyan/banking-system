package am.banking.system.user.service.auth;

import am.banking.system.common.dto.user.UserDto;
import am.banking.system.common.reponse.Result;
import am.banking.system.user.infrastructure.security.abstraction.IJwtTokenServiceClient;
import am.banking.system.user.infrastructure.security.abstraction.IUserTokenServiceClient;
import am.banking.system.user.model.dto.UserRequest;
import am.banking.system.user.model.mapper.UserMapper;
import am.banking.system.user.model.repository.UserRepository;
import am.banking.system.user.service.EmailSendingService;
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
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RequestValidation requestValidation;
    private final EmailSendingService emailSendingService;
    private final IJwtTokenServiceClient jwtTokenServiceClient;
    private final IUserTokenServiceClient userTokenServiceClient;

    public Mono<Result<String>> register(UserRequest request) {
        return requestValidation.validateRequest(request)
                .flatMap(errors -> {
                    if (!errors.isEmpty()) {
                        String message = String.join(", ", errors);
                        log.error("Validation failed: {}", message);
                        return Mono.just(Result.error(message, BAD_REQUEST.value()));
                    }

                    return userMapper.mapFromRequestToEntity(request)
                            .flatMap(userEntity -> userRepository.save(userEntity)
                                    .flatMap(savedUser -> {
                                        UserDto userDto = userMapper.mapFromEntityToDto(savedUser);

                                        return userTokenServiceClient.generateEmailVerificationToken(userDto)
                                                .flatMap(verificationToken -> emailSendingService.sendVerificationEmail(
                                                                userDto.email(),
                                                                userDto.username(),
                                                                verificationToken.token())
                                                        .then(jwtTokenServiceClient.generateJwtToken(userDto))
                                                        .doOnNext(token -> log.info("Generated JWT token: {}", token))
                                                        .thenReturn(Result.success("Your account has been registered. Please activate it by clicking the activation link we have sent to your email.")));
                                    }));
                });
    }
}