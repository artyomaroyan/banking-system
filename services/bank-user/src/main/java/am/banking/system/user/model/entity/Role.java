package am.banking.system.user.model.entity;

import am.banking.system.common.entity.BaseEntity;
import am.banking.system.user.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 13.04.25
 * Time: 23:33:25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "role")
public class Role extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String roleName;
    @DBRef
    private Set<Permission> permissions;

    public Role(RoleEnum role, Set<Permission> permission) {
        this.roleName = role.name();
        this.permissions = permission;
    }
}