package am.banking.system.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 11.06.25
 * Time: 15:53:31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usr.role_permission")
public class RolePermission {
    @Id
    @Column("id")
    private UUID id;
    @Column("role_id")
    private UUID roleId;
    @Column("permission_id")
    private UUID permissionId;

    public RolePermission(UUID roleId, UUID permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
}