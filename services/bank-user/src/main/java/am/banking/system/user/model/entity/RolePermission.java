package am.banking.system.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Author: Artyom Aroyan
 * Date: 11.06.25
 * Time: 15:53:31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_db.usr.role_permission")
public class RolePermission {
    @Column("role_id")
    private Integer roleId;
    @Column("permission_id")
    private Integer permissionId;
}