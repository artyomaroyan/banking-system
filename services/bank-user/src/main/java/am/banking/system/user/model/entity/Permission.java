package am.banking.system.user.model.entity;

import am.banking.system.common.entity.BaseEntity;
import am.banking.system.common.enums.PermissionEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "permission", schema = "usr")
public class Permission extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Enumerated(EnumType.STRING)
    private PermissionEnum permissionEnum;
}