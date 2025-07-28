package am.banking.system.user.application.service.validation;

import am.banking.system.common.shared.response.ValidationResult;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 18:17:50
 */
public interface RequestValidation<T> {
    Mono<ValidationResult> isValidRequest(T request);
}