package am.banking.system.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Author: Artyom Aroyan
 * Date: 11.06.25
 * Time: 15:53:31
 */
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission {
    private Integer roleId;
    private Integer permissionId;
}