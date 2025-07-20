package am.banking.system.account.api.controller;

import am.banking.system.common.shared.dto.account.AccountResponse;
import am.banking.system.account.application.port.in.AccountCreationUseCase;
import am.banking.system.common.shared.dto.account.AccountCreationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 16.07.25
 * Time: 23:24:21
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/current-account")
public class AccountCreationController {
    private final AccountCreationUseCase accountCreationService;

    @PostMapping("/default")
    Mono<ResponseEntity<AccountResponse>> createDefaultAccount(@Valid @RequestBody AccountCreationRequest event) {
        return accountCreationService.createDefaultAccount(event)
                .map(ResponseEntity::ok);
    }
}