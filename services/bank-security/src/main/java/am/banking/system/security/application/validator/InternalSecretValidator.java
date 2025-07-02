package am.banking.system.security.application.validator;

import am.banking.system.common.infrastructure.tls.configuration.InternalSecretProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Author: Artyom Aroyan
 * Date: 02.07.25
 * Time: 15:12:20
 */
@Component
@RequiredArgsConstructor
public class InternalSecretValidator {
    private final InternalSecretProperties properties;

    public boolean isValid(String secret) {
        return secret != null && !secret.trim().isBlank() && !secret.isEmpty() && secret.equals(properties.secret());
    }
}