package am.banking.system.user.service.auth;

import am.banking.system.user.infrastructure.security.abstraction.IJwtTokenServiceClient;
import am.banking.system.user.infrastructure.security.abstraction.IUserTokenServiceClient;
import am.banking.system.user.model.dto.UserRequest;
import am.banking.system.user.model.mapper.UserMapper;
import am.banking.system.user.model.repository.UserRepository;
import am.banking.system.user.model.result.Result;
import am.banking.system.user.service.EmailSendingService;
import am.banking.system.user.service.validation.RequestValidation;
import am.banking.system.user.service.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public Result<String> register(UserRequest request) {
        var validationMessage = validateRequest(request);
        if (!validationMessage.isEmpty()) {
            log.error("Validation failed: {}", validationMessage);
            return Result.error(String.join(", ", validationMessage), HttpStatus.BAD_REQUEST.value());
        }
        var userEntity = userMapper.mapFromRequestToEntity(request);
        userRepository.save(userEntity);
        var userDto = userMapper.mapFromEntityToDto(userEntity);
        // todo: save core account as a draft account without any permissions, until he do not activate email.
        var verificationToken = userTokenServiceClient.generateEmailVerificationToken(userDto);
        emailSendingService.sendVerificationEmail(userDto.email(), userDto.username(), verificationToken.token());
        var token = jwtTokenServiceClient.generateJwtToken(userDto);
        log.info("Generated JWT token: {}", token);
        return Result.success("Your account has been registered. Please activate it by clicking the activation link we have sent to your email.");
    }

    private List<String> validateRequest(UserRequest request) {
        List<String> error = new ArrayList<>();
        List<ValidationResult> results = List.of(
                requestValidation.isValidUsername(request.username()),
                requestValidation.isValidPassword(request.password()),
                requestValidation.isValidEmail(request.email()),
                requestValidation.isValidPhoneNumber(request.phone()));
        for (ValidationResult result : results) {
            if (!result.isValid()) {
                error.add(result.message());
            }
        }
        return error;
    }
}