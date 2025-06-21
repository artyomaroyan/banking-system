package am.banking.system.security.application.port.in;

import am.banking.system.common.shared.enums.PermissionEnum;

/**
 * Author: Artyom Aroyan
 * Date: 21.06.25
 * Time: 18:27:26
 */
public interface AuthorizationServiceUseCase {
    boolean isAuthorized(String token, PermissionEnum permission);
}