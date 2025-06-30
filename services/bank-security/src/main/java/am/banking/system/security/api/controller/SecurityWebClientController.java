package am.banking.system.security.api.controller;

import am.banking.system.common.shared.dto.security.*;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.security.api.shared.UserPrincipal;
import am.banking.system.security.application.port.in.AuthorizationServiceUseCase;
import am.banking.system.security.infrastructure.password.Argon2Hashing;
import am.banking.system.security.application.port.in.JwtTokenServiceUseCase;
import am.banking.system.security.application.port.in.UserTokenServiceUseCase;
import am.banking.system.security.application.port.in.JwtTokenValidatorUseCase;
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
 * Date: 20.04.25
 * Time: 23:58:47
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/security")
public class SecurityWebClientController {
    private final Argon2Hashing argon2Hashing;
    private final JwtTokenServiceUseCase jwtTokenService;
    private final UserTokenServiceUseCase userTokenService;
    private final JwtTokenValidatorUseCase jwtTokenValidator;
    private final UserTokenValidatorUseCase userTokenValidator;
    private final AuthorizationServiceUseCase authorizationService;

    @PostMapping("/hash-password")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('DO_INTERNAL_TASKS')")
    public ResponseEntity<PasswordHashingResponse> hashPassword(@Valid @RequestBody PasswordHashingRequest request) {
        return ResponseEntity.ok(new PasswordHashingResponse(argon2Hashing.encode(request.password())));
    }

    @PostMapping("/validate-password")
    public ResponseEntity<Boolean> validatePassword(@Valid @RequestBody PasswordValidatorRequest request) {
        boolean isValid = argon2Hashing.matches(request.rawPassword(), request.hashedPassword());
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/generate-jwt-token")
    public ResponseEntity<TokenResponse> generateJwtToken(@Valid @RequestBody UserDto userDto) {
        UserPrincipal user = new UserPrincipal(
                userDto.userId(),
                userDto.username(),
                userDto.password(),
                userDto.email(),
                userDto.roles(),
                userDto.permissions()
        );
        final String token = jwtTokenService.generateJwtToken(user);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/validate-jwt-token")
    public ResponseEntity<Boolean> validateJwtToken(@Valid @RequestBody TokenValidatorRequest request) {
        boolean isValid = jwtTokenValidator.isValidToken(request.token(), request.username());
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/generate-email-verification-token")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('DO_INTERNAL_TASKS')")
    public Mono<ResponseEntity<TokenResponse>> generateEmailVerificationToken(@Valid @RequestBody UserDto userDto) {
        UserPrincipal user = new UserPrincipal(
                userDto.userId(),
                userDto.username(),
                userDto.password(),
                userDto.email(),
                userDto.roles(),
                userDto.permissions()
        );
        return userTokenService.generateEmailVerificationToken(user)
                .map(token -> ResponseEntity.ok(new TokenResponse(token)));
    }

    @PostMapping("/validate-email-verification-token")
    public ResponseEntity<Boolean> validateEmailVerificationToken(@Valid @RequestBody TokenValidatorRequest request) {
        boolean isValid = userTokenValidator.isValidEmailVerificationToken(request.token());
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/generate-password-recovery-token")
    public Mono<ResponseEntity<TokenResponse>> generatePasswordRecoveryToken(@Valid @RequestBody UserDto userDto) {
        UserPrincipal user = new UserPrincipal(
                userDto.userId(),
                userDto.username(),
                userDto.password(),
                userDto.email(),
                userDto.roles(),
                userDto.permissions()
        );
        return userTokenService.generatePasswordResetToken(user)
                .map(token -> ResponseEntity.ok(new TokenResponse(token)));
    }

    @PostMapping("/validate-password-recovery-token")
    public ResponseEntity<Boolean> validatePasswordRecoveryToken(@Valid @RequestBody TokenValidatorRequest request) {
        boolean isValid = userTokenValidator.isValidPasswordResetToken(request.token());
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/invalidate-used-token")
    public Mono<ResponseEntity<String>> invalidateUsedToken() {
        return userTokenService.markTokensForciblyExpired()
                .thenReturn(ResponseEntity.ok("Tokens marked as forcibly expired"));
    }

    @PostMapping("/authorize")
    public ResponseEntity<Boolean> authorizeUser(@Valid @RequestBody AuthorizationRequest request) {
        return ResponseEntity.ok(authorizationService.isAuthorized(request.token(), request.permission()));
    }
}