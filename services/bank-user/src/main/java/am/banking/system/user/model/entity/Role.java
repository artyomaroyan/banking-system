package am.banking.system.user.model.entity;

import am.banking.system.common.entity.BaseEntity;
import am.banking.system.common.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;

/**
 * Author: Artyom Aroyan
 * Date: 13.04.25
 * Time: 23:33:25
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role", schema = "usr")
public class Role extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private RoleEnum roleName;
    @ManyToMany(fetch = LAZY, cascade = {MERGE, PERSIST, REFRESH})
    @JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;
}