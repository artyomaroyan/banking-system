package am.banking.system.security.permission;

import am.banking.system.security.model.entity.OwnableEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.Serializable;
import java.time.Duration;

/**
 * Author: Artyom Aroyan
 * Date: 20.04.25
 * Time: 00:40:50
 */
@Slf4j
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final WebClient webClient;

    public CustomPermissionEvaluator(@Qualifier("securedWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (targetDomainObject instanceof OwnableEntity entity) {
            return checkOwnership(authentication.getName(), entity.getOwnerUsername());
        }
        return hasPrivilege(authentication, permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        if ("User".equalsIgnoreCase(targetType) && targetId instanceof Long userId) {
            return checkUserPermission(authentication.getName(), userId);
        }
        return hasPrivilege(authentication, permission.toString());
    }

    private boolean checkOwnership(String currentUsername, String ownerUsername) {
        return currentUsername.equals(ownerUsername);
    }

    private boolean hasPrivilege(Authentication authentication, String permission) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_" + permission));
    }

    private boolean checkUserPermission(String currentUsername, Long userId) {
        try {
            return Boolean.TRUE.equals(webClient.get()
                    .uri("api/user/{userId}/verify-ownership?username={username}", userId, currentUsername)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block(Duration.ofSeconds(2)));
        } catch (Exception ex) {
            log.error("Failed to check user permission", ex);
            return false;
        }
    }
}