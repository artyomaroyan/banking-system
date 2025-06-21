package am.banking.system.user.application.service.validation;

import am.banking.system.user.api.dto.UserRequest;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 14.04.25
 * Time: 18:17:50
 */
public interface RequestValidation {
    Mono<List<String>> validateRequest(UserRequest request);
}