package am.banking.system.user.infrastructure.abstraction;

import am.banking.system.common.enums.PermissionEnum;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:32:21
 */
public interface ISecurityServiceClient {
    Boolean authorizeUser(String token, PermissionEnum permission);
}