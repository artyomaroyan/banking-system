package am.banking.system.security.api.controller;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.security.TokenValidatorRequest;
import am.banking.system.common.shared.dto.security.TokenValidatorResponse;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.security.application.mapper.UserPrincipalMapper;
import am.banking.system.security.application.port.in.UserTokenUseCase;
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
@RequestMapping("/api/internal/security/token")
public class UserTokenController {
    private final UserTokenUseCase userTokenService;
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
    public Mono<TokenValidatorResponse> validateEmailVerificationToken(@Valid @RequestBody TokenValidatorRequest request) {
        return userTokenValidator.isValidEmailVerificationToken(request.userId(), request.token())
                .map(TokenValidatorResponse::new);
    }

    @PostMapping("/access/issue")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('DO_INTERNAL_TASKS')")
    public Mono<ResponseEntity<TokenResponse>> issueJwtAccessToken(@Valid @RequestBody UserDto userDto) {
        var principal = UserPrincipalMapper.toUserPrincipal(userDto);
        return userTokenService.generateJwtAccessToken(principal)
                .map(token -> ResponseEntity.ok(new TokenResponse(token)));
    }

    @PostMapping("/access/validate")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('DO_INTERNAL_TASKS')")
    public Mono<ResponseEntity<Boolean>> validateJwtAccessToken(@RequestBody @Valid TokenValidatorRequest request) {
        return userTokenValidator.extractValidClaims(request.token())
                .hasElement()
                .map(ResponseEntity::ok);
    }

    @PostMapping("/password-reset/issue")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('DO_INTERNAL_TASKS')")
    public Mono<ResponseEntity<TokenResponse>> issuePasswordResetToken(@Valid @RequestBody UserDto userDto) {
        var principal = UserPrincipalMapper.toUserPrincipal(userDto);
        return userTokenService.generatePasswordResetToken(principal)
                .map(token -> ResponseEntity.ok(new TokenResponse(token)));
    }

    @PostMapping("/password-reset/validate")
    public Mono<TokenValidatorResponse> validatePasswordResetToken(@Valid @RequestBody TokenValidatorRequest request) {
        return userTokenValidator.isValidPasswordResetToken(request.userId(), request.token())
                .map(TokenValidatorResponse::new);
    }
}