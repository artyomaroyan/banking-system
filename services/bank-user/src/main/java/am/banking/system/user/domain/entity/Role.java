package am.banking.system.user.domain.entity;

import am.banking.system.common.shared.model.BaseEntity;
import am.banking.system.common.shared.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Author: Artyom Aroyan
 * Date: 13.04.25
 * Time: 23:33:25
 */
@Getter
@AllArgsConstructor
@Table(name = "usr.role")
public class Role extends BaseEntity {
    @Column("role_name")
    private final RoleEnum roleName;
}