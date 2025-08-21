package am.banking.system.user.application.service.access;

import am.banking.system.common.shared.enums.RoleEnum;
import am.banking.system.user.infrastructure.adapter.out.persistence.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 24.04.25
 * Time: 00:48:24
 */
@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Flux<UUID> getPermissionIdsByRole(RoleEnum roleEnum) {
        return permissionRepository.findPermissionIdsByRole(roleEnum);
    }
}