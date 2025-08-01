package am.banking.system.security.api.shared;

import am.banking.system.common.shared.enums.AccountState;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: Artyom Aroyan
 * Date: 17.04.25
 * Time: 00:35:43
 */
@Getter
public class UserPrincipal implements UserDetails {
    private final UUID userId;
    private final String username;
    private final String password;
    private final String email;
    private final Set<String> roles;
    private final Set<String> permissions;

    public UserPrincipal(UUID userId, String username, String password, String email, Set<String> roles, Set<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = Set.copyOf(roles);
        this.permissions = Set.copyOf(permissions);
    }

    public AccountState getAccountState() {
        if (!isAccountNonLocked()) {
            return AccountState.LOCKED;

        } else if (!isAccountNonExpired() || !isCredentialsNonExpired()) {
            return AccountState.INACTIVE;

        } else if (!isEnabled()) {
            return AccountState.PENDING;
        }
        return AccountState.ACTIVE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.concat(
                        roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())),
                        permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.toUpperCase())))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        // todo: implement account non expired checking logic
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // todo: implement account non lock checking logic
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // todo: implement credentials non expired checking logic
        return true;
    }

    @Override
    public boolean isEnabled() {
        // todo: implement is account enabled logic
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserPrincipal that)) return false;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username);
    }

    @Override
    public String toString() {
        return "User principal {" +
                "userId = " + userId +
                ", username = " + username +
                ", email = " + email +
                ", roles = " + roles +
                ", permissions = " + permissions +
                "}";
    }
}