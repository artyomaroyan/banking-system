package am.banking.system.user.model.entity;

import am.banking.system.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
@AllArgsConstructor
public class RolePermission extends BaseEntity {
    private Long roleId;
    private Long permissionId;
}