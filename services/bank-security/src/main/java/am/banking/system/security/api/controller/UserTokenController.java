package am.banking.system.security.api.controller;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.security.TokenValidatorRequest;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.security.application.mapper.UserPrincipalMapper;
import am.banking.system.security.application.port.in.UserTokenServiceUseCase;
import am.banking.system.security.application.port.in.UserTokenValidatorUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 02.07.25
 * Time: 15:32:26
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/security/user-token")
public class UserTokenController {
    private final UserTokenServiceUseCase userTokenService;
    private final UserTokenValidatorUseCase userTokenValidator;

    @PostMapping("/email/issue")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('DO_INTERNAL_TASKS')")
    public Mono<ResponseEntity<TokenResponse>> issueEmailVerificationToken(@Valid @RequestBody UserDto userDto) {
        var principal = UserPrincipalMapper.toUserPrincipal(userDto);
        return userTokenService.generateEmailVerificationToken(principal)
                .map(token -> ResponseEntity.ok(new TokenResponse(token)));
    }

    @PostMapping("/email/validate")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('DO_INTERNAL_TASKS')")
    public Mono<Boolean> validateEmailVerificationToken(@Valid @RequestBody TokenValidatorRequest request) {
        return Mono.fromCallable(() ->
                        userTokenValidator.isValidEmailVerificationToken(request.token()));
    }

    @PostMapping("/password-reset/issue")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('DO_INTERNAL_TASKS')")
    public Mono<ResponseEntity<TokenResponse>> issuePasswordResetToken(@Valid @RequestBody UserDto userDto) {
        var principal = UserPrincipalMapper.toUserPrincipal(userDto);
        return userTokenService.generatePasswordResetToken(principal)
                .map(token -> ResponseEntity.ok(new TokenResponse(token)));
    }

    @PostMapping("/password-reset/validate")
    public Mono<ResponseEntity<Boolean>> validatePasswordResetToken(@Valid @RequestBody TokenValidatorRequest request) {
        return Mono.fromCallable(() ->
                        userTokenValidator.isValidPasswordResetToken(request.token()))
                .map(ResponseEntity::ok);
    }
}