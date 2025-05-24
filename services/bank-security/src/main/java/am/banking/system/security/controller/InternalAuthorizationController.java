package am.banking.system.security.controller;

import am.banking.system.security.token.service.abstraction.IJwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Artyom Aroyan
 * Date: 17.05.25
 * Time: 01:31:11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/secure/local/")
public class InternalAuthorizationController {
    private final IJwtTokenService jwtTokenService;

    @PostMapping("system-token")
    @PreAuthorize("hasAuthority('GENERATE_SYSTEM_TOKEN')")
    public ResponseEntity<String> generateSystemToken() {
        return ResponseEntity.ok(jwtTokenService.generateSystemToken());
    }
}