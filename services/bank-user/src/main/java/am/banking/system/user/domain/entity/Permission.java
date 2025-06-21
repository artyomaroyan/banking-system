package am.banking.system.user.domain.entity;

import am.banking.system.common.shared.model.BaseEntity;
import am.banking.system.common.shared.enums.PermissionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Author: Artyom Aroyan
 * Date: 13.04.25
 * Time: 23:41:39
 */
@Getter
@AllArgsConstructor
@Table(name = "user_db.usr.permission")
public class Permission extends BaseEntity {
    @Column("permission_name")
    private final PermissionEnum permissionName;
}