package am.banking.system.user.model.entity;

import am.banking.system.common.entity.BaseEntity;
import am.banking.system.common.enums.PermissionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Artyom Aroyan
 * Date: 13.04.25
 * Time: 23:41:39
 */
@Getter
@AllArgsConstructor
@Table(name = "permission")
public class Permission extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final PermissionEnum permissionName;
}