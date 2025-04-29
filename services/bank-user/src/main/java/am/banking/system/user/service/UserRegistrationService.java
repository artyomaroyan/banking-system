package am.banking.system.user.service;

import am.banking.system.user.infrastructure.client.SecurityServiceClient;
import am.banking.system.user.model.dto.UserRequest;
import am.banking.system.user.model.dto.UserResponse;
import am.banking.system.user.model.mapper.UserMapper;
import am.banking.system.user.model.repository.UserRepository;
import am.banking.system.user.model.result.Result;
import am.banking.system.user.service.validation.RequestValidation;
import am.banking.system.user.service.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final SecurityServiceClient securityServiceClient;

    public Result<UserResponse> register(UserRequest request) {
        var validationMessage = validateRequest(request);
        if (!validationMessage.isEmpty()) {
            log.error("Validation failed: {}", validationMessage);
            return Result.error(String.join(", ", validationMessage));
        }
        var user = userMapper.mapFromRequestToEntity(request);
        userRepository.save(user);
        var userDto = userMapper.mapFromEntityToDto(user);
        var verificationToken = securityServiceClient.generateEmailVerificationToken(userDto);
        // todo: generate email verification token
        // todo: send verification email
        // todo: generate jwt token

        return Result.success("");
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