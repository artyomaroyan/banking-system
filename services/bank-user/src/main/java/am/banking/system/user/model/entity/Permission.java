package am.banking.system.user.model.entity;

import am.banking.system.common.entity.BaseEntity;
import am.banking.system.common.enums.PermissionEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 13.04.25
 * Time: 23:41:39
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permission", schema = "bank-user")
public class Permission extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private PermissionEnum permissionName;
    @ManyToMany(mappedBy = "permission")
    private Set<Role> roles;

    public Permission(PermissionEnum permissionName) {
        this.permissionName = permissionName;
    }
}