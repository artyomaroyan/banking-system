package am.banking.system.security.api.controller;

import am.banking.system.common.shared.dto.security.TokenResponse;
import am.banking.system.common.shared.dto.security.TokenValidatorRequest;
import am.banking.system.common.shared.dto.user.UserDto;
import am.banking.system.security.application.mapper.UserPrincipalMapper;
import am.banking.system.security.application.port.in.JwtTokenServiceUseCase;
import am.banking.system.security.application.port.in.JwtTokenValidatorUseCase;
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
 * Time: 14:16:55
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/security/jwt")
public class JwtTokenController {
    private final JwtTokenServiceUseCase jwtTokenService;
    private final JwtTokenValidatorUseCase jwtTokenValidator;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('DO_INTERNAL_TASKS')")
    public Mono<ResponseEntity<TokenResponse>> generate(@Valid @RequestBody UserDto userDto) {
        var principal = UserPrincipalMapper.toUserPrincipal(userDto);
        return Mono.fromCallable(() -> jwtTokenService.generateJwtToken(principal))
                .map(token -> ResponseEntity.ok(new TokenResponse(token)));
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('DO_INTERNAL_TASKS')")
    public Mono<ResponseEntity<Boolean>> validate(@RequestBody @Valid TokenValidatorRequest request) {
        return Mono.fromCallable(() ->
                        jwtTokenValidator.isValidToken(request.token()))
                .map(ResponseEntity::ok);
    }
}