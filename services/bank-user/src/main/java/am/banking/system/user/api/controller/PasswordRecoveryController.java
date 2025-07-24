package am.banking.system.user.api.controller;

import am.banking.system.user.application.port.in.password.PasswordRecoveryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Artyom Aroyan
 * Date: 24.07.25
 * Time: 22:30:31
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PasswordRecoveryController {
    private final PasswordRecoveryUseCase passwordRecoveryService;

}