package am.banking.system.security.model.dto;

import am.banking.system.common.enums.AccountState;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Author: Artyom Aroyan
 * Date: 17.04.25
 * Time: 00:35:43
 */
@Getter
public class UserPrincipal implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long userId;
    private final String username;
    private final String password;
    private final String email;
    private final Set<String> roles;
    private final Set<String> permissions;

    public UserPrincipal(Long userId, String username, String password, String email, Set<String> roles, Set<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = Collections.unmodifiableSet(roles);
        this.permissions = Collections.unmodifiableSet(permissions);
    }

    public AccountState getAccountState() {
        if (!isAccountNonLocked() || !isAccountNonExpired() || !isCredentialsNonExpired() || !isEnabled()) {
            return AccountState.LOCKED;
        }
        return AccountState.ACTIVE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
        permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority("ROLE_" + permission.toUpperCase())));
        return authorities;
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