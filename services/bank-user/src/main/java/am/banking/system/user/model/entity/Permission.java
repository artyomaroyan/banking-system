package am.banking.system.user.model.entity;

import am.banking.system.common.entity.BaseEntity;
import am.banking.system.common.enums.PermissionEnum;
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
 * Time: 23:41:39
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "permission")
public class Permission extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private PermissionEnum permissionName;
    @DBRef
    private Set<Role> roles;

    public Permission(PermissionEnum permissionName) {
        this.permissionName = permissionName;
    }
}