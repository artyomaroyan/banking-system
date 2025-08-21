package am.banking.system.user.domain.model;

import am.banking.system.common.shared.model.BaseEntity;
import am.banking.system.common.shared.enums.PermissionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Author: Artyom Aroyan
 * Date: 13.04.25
 * Time: 23:41:39
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usr.permission")
public class Permission extends BaseEntity {
    @Column("permission_name")
    private PermissionEnum permissionName;
}