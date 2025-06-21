package am.banking.system.user.application.service.user.access;

import am.banking.system.common.shared.enums.RoleEnum;
import am.banking.system.user.domain.entity.Permission;
import am.banking.system.user.domain.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Author: Artyom Aroyan
 * Date: 24.04.25
 * Time: 00:48:24
 */
@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Flux<Permission> getPermissionsByRole(RoleEnum roleEnum) {
        return permissionRepository.findPermissionByRole(roleEnum);
    }
}